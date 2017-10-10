package itomy.sigterra.web.rest;

import itomy.sigterra.annotation.Analytic;
import itomy.sigterra.domain.enumeration.EventType;
import itomy.sigterra.repository.CardletRepository;
import itomy.sigterra.repository.ItemRepository;
import itomy.sigterra.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

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

    @Inject
    private EventService eventService;
    @Inject
    private CardletRepository cardletRepository;
    @Inject
    private ItemRepository itemRepository;

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

    @PostMapping("/item/{id:\\d+}")
    @Analytic(type = {EventType.CLICK, EventType.READ})
    public ResponseEntity clickItem(@PathVariable Long id) {
        log.debug("REST request to click item with id = {}, from user with ip = {} and agent = {}", id, getRequestIp(), getRequestUserAgent());

        if (not(itemRepository.exists(id))) {
            return errorResponse(HttpStatus.NOT_FOUND, "Item not found");
        }
        try {
            eventService.clickItem(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException ex) {
            return errorResponse(BAD_REQUEST, ex.getMessage());
        }
    }

    private static boolean not(boolean statement) {
        return !statement;
    }
}
