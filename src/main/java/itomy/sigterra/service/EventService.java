package itomy.sigterra.service;

import itomy.sigterra.domain.*;
import itomy.sigterra.domain.enumeration.EventType;
import itomy.sigterra.repository.CardletRepository;
import itomy.sigterra.repository.EventRepository;
import itomy.sigterra.repository.ItemRepository;
import itomy.sigterra.domain.TopDomain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import static itomy.sigterra.web.rest.util.ResponseUtil.errorResponse;
import static itomy.sigterra.web.rest.util.ResponseUtil.okResponse;

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
    private VisitorService visitorService;

    public ResponseEntity clickCardlet(Long cardletId) {
        Cardlet cardlet = cardletRepository.findOne(cardletId);
        if (cardlet == null) {
            return errorResponse(HttpStatus.BAD_REQUEST, "Cardlet not found.");
        }
        Visitor visitor = getVisitor();

        // Check if click on own Cardlet
        if (isCardletOwner(cardlet, visitor)) {
            return errorResponse(HttpStatus.BAD_REQUEST, "Self clicks are not counting.");
        }

        Event event = fromTemplate(visitor, cardlet, EventType.CLICK, null);
        eventRepository.save(event);
        return okResponse();
    }

    public ResponseEntity clickItem(Long itemId) {
        Item item = itemRepository.findOne(itemId);
        if (item == null) {
            return errorResponse(HttpStatus.BAD_REQUEST, "Item not found");
        }
        Visitor visitor = getVisitor();

        // Check if click on own Cardlet
        if (isCardletOwner(item.getCardlet(), visitor)) {
            return errorResponse(HttpStatus.BAD_REQUEST, "Self clicks are not counting.");
        }

        Event eventClickItem = fromTemplate(visitor, item.getCardlet(), EventType.CLICK, item);
        Event eventReadItem = fromTemplate(visitor, item.getCardlet(), EventType.READ, item);

        eventRepository.save(eventClickItem);
        eventRepository.save(eventReadItem);
        return okResponse();
    }

    public void addContactEvent(Cardlet cardlet) {
        Visitor visitor = getVisitor();

        if (isCardletOwner(cardlet, visitor)) return; // Check if click on own Cardlet

        Event event = fromTemplate(visitor, cardlet, EventType.ADD, null);
        eventRepository.save(event);
    }

    public void viewEvent(Long cardletId) {
        Cardlet cardlet = cardletRepository.findOne(cardletId);
        Visitor visitor = visitorService.getOrCreate();

        if (isCardletOwner(cardlet, visitor)) return; // Check if click on own Cardlet

        Event event = fromTemplate(visitor, cardlet, EventType.VIEW, null);
        eventRepository.save(event);
    }

    public List<Event> getAllEvents(Cardlet cardlet, Timestamp dateFrom) {
        return eventRepository.findAllEvents(cardlet, dateFrom);
    }

    public List<Event> getAllEvents(Timestamp dateFrom) {
        return eventRepository.findAllEvents(getVisitor().getUser(), dateFrom);
    }

    Page<TopDomain> getAllClicksByCardlet(Cardlet cardlet, Timestamp dateFrom, Pageable pageable) {
        return eventRepository.findAllClicksByCardlet(cardlet, dateFrom, pageable);
    }

    Page<TopDomain> getAllClicksForUserCardlets(Timestamp dateFrom, Pageable pageable) {
        return eventRepository.findAllClicksForUserCardlets(getVisitor().getUser(), dateFrom, pageable);
    }

    Page<Event> getAllRecentsByCardlet(Cardlet cardlet, List<EventType> types, Pageable pageable) {
        return eventRepository.findAllRecentsByCardlet(cardlet, types, pageable);
    }

    Page<Event> getAllRecentForUser(List<EventType> types, Pageable pageable) {
        return eventRepository.findAllRecentForUser(getVisitor().getUser(), types, pageable);
    }

    private Visitor getVisitor() {
        return visitorService.getOrCreate();
    }

    private Event fromTemplate(Visitor visitor, Cardlet cardlet, EventType type, Item item) {
        Event event = new Event();
        event.setType(type);
        event.setVisitor(visitor);
        event.setCardlet(cardlet);
        if (item != null) {
            event.setItem(item);
        }
        event.setCreatedDate(new Timestamp(Instant.now().toEpochMilli()));
        return event;
    }

    private boolean isCardletOwner(Cardlet cardlet, Visitor visitor) {
        User user = visitor.getUser();
        return user != null && user.equals(cardlet.getUser());
    }
}
