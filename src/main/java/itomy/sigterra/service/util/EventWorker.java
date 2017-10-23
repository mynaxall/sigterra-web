package itomy.sigterra.service.util;

import itomy.sigterra.config.JHipsterProperties;
import itomy.sigterra.domain.Event;
import itomy.sigterra.domain.User;
import itomy.sigterra.domain.Visitor;
import itomy.sigterra.repository.CardletRepository;
import itomy.sigterra.repository.EventRepository;
import itomy.sigterra.repository.ItemRepository;
import itomy.sigterra.service.VisitorService;
import itomy.sigterra.service.dto.EventProcessDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static itomy.sigterra.domain.enumeration.EventType.READ;

@Service
public class EventWorker {
    private static final Logger log = LoggerFactory.getLogger(EventWorker.class);

    private final Queue<EventProcessDTO> queue = new LinkedBlockingQueue<>();

    private VisitorService visitorService;
    private EventRepository eventRepository;
    private CardletRepository cardletRepository;
    private ItemRepository itemRepository;

    private final Integer processEventDelay;
    private final Integer processEventInitialDelay;
    private final TimeUnit processEventDelayTimeUnit;

    public EventWorker(VisitorService visitorService,
                       EventRepository eventRepository,
                       JHipsterProperties jHipsterProperties,
                       CardletRepository cardletRepository,
                       ItemRepository itemRepository) {
        this.visitorService = visitorService;
        this.eventRepository = eventRepository;
        this.cardletRepository = cardletRepository;
        this.itemRepository = itemRepository;

        JHipsterProperties.EventWorker eventWorker = jHipsterProperties.getEventWorker();
        this.processEventDelay = eventWorker.getProcessEventDelay();
        this.processEventInitialDelay = eventWorker.getProcessEventInitialDelay();
        this.processEventDelayTimeUnit = eventWorker.getProcessEventDelayTimeUnit();
    }

    @PostConstruct
    public void init() {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleWithFixedDelay(this::process, processEventInitialDelay, processEventDelay, processEventDelayTimeUnit);
    }

    public void processEvent(EventProcessDTO eventProcessDTO) {
        Event event = eventProcessDTO.getEvent();
        if (event != null) {
            queue.offer(eventProcessDTO);
        }
    }

    private void process() {
        while (!queue.isEmpty()) {
            EventProcessDTO eventProcessDTO = queue.poll();
            Event event = eventProcessDTO.getEvent();
            try {
                if (event != null) {
                    Visitor visitor = getVisitor(eventProcessDTO);
                    event.setVisitor(visitor);
                    trySaveEvent(event);
                }
            } catch (Exception ex) {
                log.error("Can't process event", ex);
            }
        }
    }

    private void trySaveEvent(Event event) {
        if (event.getType().equals(READ)) {
            if (cardletRepository.exists(event.getCardlet().getId())
                && itemRepository.exists(event.getItem().getId())) {
                eventRepository.save(event);
            }
        } else {
            if (cardletRepository.exists(event.getCardlet().getId())) {
                eventRepository.save(event);
            }
        }
    }

    private Visitor getVisitor(EventProcessDTO dto) {
        User user = dto.getUser();
        String ip = dto.getIp();
        String agent = dto.getAgent();

        return visitorService.getOrCreate(user, ip, agent);
    }
}
