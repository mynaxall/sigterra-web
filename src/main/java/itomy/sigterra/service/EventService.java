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

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Service Implementation for managing Event.
 */
@Service
@Transactional
public class EventService {

    private ItemRepository itemRepository;
    private EventRepository eventRepository;
    private CardletRepository cardletRepository;
    private UserService userService;
    private EventWorker eventWorker;

    public EventService(ItemRepository itemRepository,
                        EventRepository eventRepository,
                        CardletRepository cardletRepository,
                        UserService userService,
                        EventWorker eventWorker) {

        this.itemRepository = itemRepository;
        this.eventRepository = eventRepository;
        this.cardletRepository = cardletRepository;
        this.userService = userService;
        this.eventWorker = eventWorker;
    }

    public void clickCardlet(Long cardletId) {
        Cardlet cardlet = cardletRepository.findOne(cardletId);

        // Check if click on own Cardlet
        if (isCardletOwner(cardlet)) {
            throw new IllegalArgumentException("Self clicks are not counting.");
        }

        Event event = fromTemplate(cardlet, EventType.CLICK);
        eventWorker.processEvent(convertToDTO(event));
    }

    public void clickItem(Long itemId, Long itemDataId) {
        Item item = itemRepository.findOne(itemId);

        String link = getLink(item, itemDataId);

        // Check if click on own Item
        if (isCardletOwner(item.getCardlet())) {
            throw new IllegalArgumentException("Self clicks are not counting.");
        }

        Event eventClickItem = fromTemplate(item.getCardlet(), EventType.CLICK, item, null);
        Event eventReadItem = fromTemplate(item.getCardlet(), EventType.READ, item, link);

        eventWorker.processEvent(convertToDTO(eventClickItem));
        eventWorker.processEvent(convertToDTO(eventReadItem));
    }

    public void addContactEvent(Cardlet cardlet) {
        if (isCardletOwner(cardlet)) return; // Check if add own Cardlet

        Event event = fromTemplate(cardlet, EventType.ADD);
        eventWorker.processEvent(convertToDTO(event));
    }

    public void viewEvent(Long cardletId) {
        Cardlet cardlet = cardletRepository.findOne(cardletId);

        if (isCardletOwner(cardlet)) return; // Check if open own Cardlet

        Event event = fromTemplate(cardlet, EventType.VIEW);
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

    private String getLink(Item item, Long itemDataId) {
        Optional<ItemData> itemData = item.getItemData().stream()
            .filter(Objects::nonNull)
            .filter(data -> data.getId().equals(itemDataId))
            .findFirst();

        return itemData.map(ItemData::getLink)
            .orElse(null);
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

    private Event fromTemplate(Cardlet cardlet, EventType type) {
        Event event = new Event();
        event.setType(type);
        event.setCardlet(cardlet);
        event.setCreatedDate(new Timestamp(Instant.now().toEpochMilli()));
        return event;
    }

    private Event fromTemplate(Cardlet cardlet, EventType type, Item item, String description) {
        Event event = new Event();
        event.setType(type);
        event.setCardlet(cardlet);
        if (item != null) {
            event.setItem(item);
        }
        event.setCreatedDate(new Timestamp(Instant.now().toEpochMilli()));
        event.setDescription(description);
        return event;
    }

    boolean isCardletOwner(Cardlet cardlet) {
        User user = getUser();
        return user != null && user.equals(cardlet.getUser());
    }
}
