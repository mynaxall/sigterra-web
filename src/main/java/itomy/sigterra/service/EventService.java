package itomy.sigterra.service;

import itomy.sigterra.domain.*;
import itomy.sigterra.domain.enumeration.EventType;
import itomy.sigterra.repository.CardletRepository;
import itomy.sigterra.repository.EventRepository;
import itomy.sigterra.repository.ItemRepository;
import itomy.sigterra.service.dto.EventProcessDTO;
import itomy.sigterra.service.util.EventWorker;
import itomy.sigterra.service.util.RequestUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

/**
 * Service Implementation for managing Event.
 */
@Service
@Transactional
public class EventService {

    @Inject
    private ItemRepository itemRepository;
    @Inject
    private EventRepository eventRepository;
    @Inject
    private CardletRepository cardletRepository;
    @Inject
    private UserService userService;
    @Inject
    private EventWorker eventWorker;

    public void clickCardlet(Long cardletId) {
        Cardlet cardlet = cardletRepository.findOne(cardletId);

        // Check if click on own Cardlet
        if (isCardletOwner(cardlet)) {
            throw new IllegalArgumentException("Self clicks are not counting.");
        }

        Event event = fromTemplate(cardlet, EventType.CLICK, null);
        eventWorker.processEvent(convertToDTO(event));
    }

    public void clickItem(Long itemId) {
        Item item = itemRepository.findOne(itemId);

        // Check if click on own Item
        if (isCardletOwner(item.getCardlet())) {
            throw new IllegalArgumentException("Self clicks are not counting.");
        }

        Event eventClickItem = fromTemplate(item.getCardlet(), EventType.CLICK, item);
        Event eventReadItem = fromTemplate(item.getCardlet(), EventType.READ, item);

        eventWorker.processEvent(convertToDTO(eventClickItem));
        eventWorker.processEvent(convertToDTO(eventReadItem));
    }

    public void addContactEvent(Cardlet cardlet) {
        if (isCardletOwner(cardlet)) return; // Check if add own Cardlet

        Event event = fromTemplate(cardlet, EventType.ADD, null);
        eventWorker.processEvent(convertToDTO(event));
    }

    public void viewEvent(Long cardletId) {
        Cardlet cardlet = cardletRepository.findOne(cardletId);

        if (isCardletOwner(cardlet)) return; // Check if open own Cardlet

        Event event = fromTemplate(cardlet, EventType.VIEW, null);
        eventWorker.processEvent(convertToDTO(event));
    }

    List<Event> getAllEvents(Cardlet cardlet, Timestamp dateFrom) {
        return eventRepository.findAllEvents(cardlet, dateFrom);
    }

    List<Event> getAllEvents(Timestamp dateFrom) {
        return eventRepository.findAllEvents(getUser(), dateFrom);
    }

    Page<TopDomain> getAllClicksByCardlet(Cardlet cardlet, Timestamp dateFrom, Pageable pageable) {
        return eventRepository.findAllClicksByCardlet(cardlet, dateFrom, pageable);
    }

    Page<TopDomain> getAllClicksForUserCardlets(Timestamp dateFrom, Pageable pageable) {
        return eventRepository.findAllClicksForUserCardlets(getUser(), dateFrom, pageable);
    }

    Page<Event> getAllRecentsByCardlet(Cardlet cardlet, List<EventType> types, Pageable pageable) {
        return eventRepository.findAllRecentsByCardlet(cardlet, types, pageable);
    }

    Page<Event> getAllRecentForUser(List<EventType> types, Pageable pageable) {
        return eventRepository.findAllRecentForUser(getUser(), types, pageable);
    }

    private User getUser() {
        return userService.getUserWithAuthorities();
    }

    private EventProcessDTO convertToDTO(Event event) {
        User user = getUser();
        String ip = RequestUtil.getRequestIp();
        String agent = RequestUtil.getRequestUserAgent();

        return new EventProcessDTO(event, user, ip, agent);
    }

    private Event fromTemplate(Cardlet cardlet, EventType type, Item item) {
        Event event = new Event();
        event.setType(type);
        event.setCardlet(cardlet);
        if (item != null) {
            event.setItem(item);
        }
        event.setCreatedDate(new Timestamp(Instant.now().toEpochMilli()));
        return event;
    }

    boolean isCardletOwner(Cardlet cardlet) {
        User user = getUser();
        return user != null && user.equals(cardlet.getUser());
    }
}
