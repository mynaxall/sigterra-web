package itomy.sigterra.web.rest;

import itomy.sigterra.annotation.Analytic;
import itomy.sigterra.domain.enumeration.EventType;
import itomy.sigterra.repository.CardletRepository;
import itomy.sigterra.repository.ItemDataRepository;
import itomy.sigterra.repository.ItemRepository;
import itomy.sigterra.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static itomy.sigterra.service.util.RequestUtil.getRequestIp;
import static itomy.sigterra.service.util.RequestUtil.getRequestUserAgent;
import static itomy.sigterra.web.rest.util.ResponseUtil.errorResponse;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * REST controller for managing Event.
 */
@RestController
@RequestMapping("/api/event")
public class EventResource {

    private final Logger log = LoggerFactory.getLogger(EventResource.class);

    private final EventService eventService;
    private final CardletRepository cardletRepository;
    private final ItemRepository itemRepository;
    private final ItemDataRepository itemDataRepository;

    public EventResource(EventService eventService,
                         CardletRepository cardletRepository,
                         ItemRepository itemRepository,
                         ItemDataRepository itemDataRepository) {

        this.eventService = eventService;
        this.cardletRepository = cardletRepository;
        this.itemRepository = itemRepository;
        this.itemDataRepository = itemDataRepository;
    }

    @PostMapping("/cardlet/{id:\\d+}")
    @Analytic(type = EventType.CLICK)
    public ResponseEntity clickCardlet(@PathVariable Long id) {
        log.debug("REST request to click cardlet with id = {}, from user with ip = {} and agent = {}", id, getRequestIp(), getRequestUserAgent());

        if (not(cardletRepository.exists(id))) {
            return errorResponse(HttpStatus.NOT_FOUND, "Cardlet not found");
        }
        try {
            eventService.clickCardlet(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException ex) {
            return errorResponse(BAD_REQUEST, ex.getMessage());
        }
    }

    @PostMapping("/item/{itemId:\\d+}/{itemDataId:\\d+}")
    @Analytic(type = {EventType.CLICK, EventType.READ})
    public ResponseEntity clickItem(@PathVariable Long itemId,
                                    @PathVariable Long itemDataId) {
        log.debug("REST request to click item with id = {}, item data with id = {}, from user with ip = {} and agent = {}",
            itemId, itemDataId, getRequestIp(), getRequestUserAgent());

        if (not(itemRepository.exists(itemId)) || not(itemDataRepository.exists(itemDataId))) {
            return errorResponse(HttpStatus.NOT_FOUND, "Item not found");
        }
        try {
            eventService.clickItem(itemId, itemDataId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException ex) {
            return errorResponse(BAD_REQUEST, ex.getMessage());
        }
    }

    @PostMapping("/item/pdf/click/{itemId:\\d+}/{itemDataId:\\d+}")
    @Analytic(type = {EventType.PDF_CLICK})
    public ResponseEntity clickItemPdf(@PathVariable Long itemId,
                                       @PathVariable Long itemDataId) {
        log.debug("REST request to click item with id = {}, item data with id = {}", itemId, itemDataId);

        if (!(itemRepository.exists(itemId)) || !(itemDataRepository.exists(itemDataId))) {
            return errorResponse(HttpStatus.NOT_FOUND, "Item not found");
        }
        try {
            eventService.clickItemPdf(itemId, itemDataId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException ex) {
            return errorResponse(BAD_REQUEST, ex.getMessage());
        }
    }

    @PostMapping("/item/pdf/read/{itemId:\\d+}/{itemDataId:\\d+}")
    @Analytic(type = {EventType.PDF_READ})
    public ResponseEntity readItemPdf(@PathVariable Long itemId,
                                       @PathVariable Long itemDataId) {
        log.debug("REST request to read item with id = {}, item data with id = {}", itemId, itemDataId);

        if (!(itemRepository.exists(itemId)) || !(itemDataRepository.exists(itemDataId))) {
            return errorResponse(HttpStatus.NOT_FOUND, "Item not found");
        }
        try {
            eventService.readItemPdf(itemId, itemDataId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException ex) {
            return errorResponse(BAD_REQUEST, ex.getMessage());
        }
    }

    private static boolean not(boolean statement) {
        return !statement;
    }
}
