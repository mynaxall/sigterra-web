package itomy.sigterra.web.rest;

import itomy.sigterra.annotation.Analytic;
import itomy.sigterra.domain.enumeration.EventType;
import itomy.sigterra.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import static itomy.sigterra.service.util.RequestUtil.getRequestIp;
import static itomy.sigterra.service.util.RequestUtil.getRequestUserAgent;

/**
 * REST controller for managing Event.
 */
@RestController
@RequestMapping("/api/event")
public class EventResource {

    private final Logger log = LoggerFactory.getLogger(EventResource.class);

    @Inject
    private EventService eventService;

    @PostMapping("/cardlet/{id:\\d+}")
    @Analytic(type = EventType.CLICK)
    public ResponseEntity clickCardlet(@PathVariable Long id) {
        log.debug("REST request to click cardlet with id = {}, from user with ip = {} and agent = {}", id, getRequestIp(), getRequestUserAgent());

        return eventService.clickCardlet(id);
    }

    @PostMapping("/item/{id:\\d+}")
    @Analytic(type = {EventType.CLICK, EventType.READ})
    public ResponseEntity clickItem(@PathVariable Long id) {
        log.debug("REST request to click item with id = {}, from user with ip = {} and agent = {}", id, getRequestIp(), getRequestUserAgent());

        return eventService.clickItem(id);
    }
}
