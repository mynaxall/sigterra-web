package itomy.sigterra.web.rest;

import com.codahale.metrics.annotation.Timed;
import itomy.sigterra.domain.CardletQuickBitesWidget;
import itomy.sigterra.repository.CardletQuickBitesWidgetRepository;
import itomy.sigterra.repository.CardletTestimonialWidgetRepository;
import itomy.sigterra.service.CardletWidgetService;
import itomy.sigterra.web.rest.errors.BadRequestAlertException;
import itomy.sigterra.web.rest.util.HeaderUtil;
import itomy.sigterra.web.rest.util.PaginationUtil;
import itomy.sigterra.web.rest.vm.CardletQuickBitesWidgetRequestVM;
import itomy.sigterra.web.rest.vm.CardletQuickBitesWidgetResponseVM;
import itomy.sigterra.web.rest.vm.CardletTestimonialWidgetRequestVM;
import itomy.sigterra.web.rest.vm.CardletTestimonialWidgetResponseVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing CardletTestimonialWidget.
 */
@RestController
@RequestMapping("/api")
public class CardletWidgetResource {

    private final Logger log = LoggerFactory.getLogger(CardletWidgetResource.class);

    private static final String ENTITY_NAME_TESTIOMONIAL = "cardletTestimonialWidget";
    private static final String ENTITY_NAME_QUICK_BITES = "cardletQuickBitesWidget";


    @Inject
    private CardletTestimonialWidgetRepository cardletTestimonialWidgetRepository;

    @Inject
    private CardletQuickBitesWidgetRepository cardletQuickBitesWidgetRepository;

    @Inject
    private CardletWidgetService cardletWidgetService;

    /**
     * POST  /cardlet-testimonial-widgets : Create a new cardletTestimonialWidget.
     *
     * @param cardletTestimonialWidget the cardletTestimonialWidget to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cardletTestimonialWidget, or with status 400 (Bad Request) if the cardletTestimonialWidget has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cardlet-testimonial-widgets")
    @Timed
    public ResponseEntity<CardletTestimonialWidgetResponseVM> createCardletTestimonialWidget(@Valid @RequestBody CardletTestimonialWidgetRequestVM cardletTestimonialWidget) throws URISyntaxException {
        log.debug("REST request to save CardletTestimonialWidget : {}", cardletTestimonialWidget);
        if (cardletTestimonialWidget.getCardletId() == null) {
            return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert(ENTITY_NAME_TESTIOMONIAL, "idexists", "A new cardletTestimonialWidget cannot already have an ID"))
                .body(null);
        }
        CardletTestimonialWidgetResponseVM result = cardletWidgetService.saveCardletTestimonialWidget(cardletTestimonialWidget);
        return ResponseEntity.created(new URI("/api/cardlet-testimonial-widgets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME_TESTIOMONIAL, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cardlet-testimonial-widgets : Updates an existing cardletTestimonialWidget.
     *
     * @param cardletTestimonialWidget the cardletTestimonialWidget to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cardletTestimonialWidget,
     * or with status 400 (Bad Request) if the cardletTestimonialWidget is not valid,
     * or with status 500 (Internal Server Error) if the cardletTestimonialWidget couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cardlet-testimonial-widgets")
    @Timed
    public ResponseEntity<CardletTestimonialWidgetResponseVM> updateCardletTestimonialWidget(@Valid @RequestBody CardletTestimonialWidgetRequestVM cardletTestimonialWidget) throws URISyntaxException {
        log.debug("REST request to update CardletTestimonialWidget : {}", cardletTestimonialWidget);
        if (cardletTestimonialWidget.getId() == null) {
            return createCardletTestimonialWidget(cardletTestimonialWidget);
        }
        CardletTestimonialWidgetResponseVM result = cardletWidgetService.saveCardletTestimonialWidget(cardletTestimonialWidget);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME_TESTIOMONIAL, cardletTestimonialWidget.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cardlet-testimonial-widgets : get all the cardletTestimonialWidgets.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of cardletTestimonialWidgets in body
     */
    @GetMapping("/cardlet-testimonial-widgets/all/cardlet/{cardletId}")
    @Timed
    public ResponseEntity<List<CardletTestimonialWidgetResponseVM>> getAllCardletTestimonialWidgets(@PathVariable Long cardletId, Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get a page of CardletTestimonialWidgets");
        Page<CardletTestimonialWidgetResponseVM> page = cardletWidgetService.findAllByCardletId(cardletId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cardlet-testimonial-widgets/all/cardlet/" + cardletId);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /cardlet-testimonial-widgets/:id : get the "id" cardletTestimonialWidget.
     *
     * @param id the id of the cardletTestimonialWidget to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cardletTestimonialWidget, or with status 404 (Not Found)
     */
    @GetMapping("/cardlet-testimonial-widgets/{id}/cardlet/{cardletId}")
    @Timed
    public ResponseEntity<CardletTestimonialWidgetResponseVM> getCardletTestimonialWidget(@PathVariable Long id, @PathVariable Long cardletId) {
        log.debug("REST request to get CardletTestimonialWidget : {}", id);
        CardletTestimonialWidgetResponseVM cardletTestimonialWidget = cardletWidgetService.findTestimonialWidget(id, cardletId);

        return Optional.ofNullable(cardletTestimonialWidget)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /cardlet-testimonial-widgets/:id : delete the "id" cardletTestimonialWidget.
     *
     * @param id the id of the cardletTestimonialWidget to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cardlet-testimonial-widgets/{id}")
    @Timed
    public ResponseEntity<Void> deleteCardletTestimonialWidget(@PathVariable Long id) {
        log.debug("REST request to delete CardletTestimonialWidget : {}", id);
        cardletTestimonialWidgetRepository.deleteById(id);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME_TESTIOMONIAL, id.toString()))
            .build();
    }


    /**
     * POST  /cardlet-quick-bites-widgets : Create a new cardletQuickBitesWidget.
     *
     * @param cardletQuickBitesWidget the cardletQuickBitesWidget to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cardletQuickBitesWidget, or with status 400 (Bad Request) if the cardletQuickBitesWidget has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cardlet-quick-bites-widgets")
    @Timed
    public ResponseEntity<CardletQuickBitesWidgetResponseVM> createCardletQuickBitesWidget(@Valid @RequestBody CardletQuickBitesWidgetRequestVM cardletQuickBitesWidget) throws URISyntaxException {
        log.debug("REST request to save CardletQuickBitesWidget : {}", cardletQuickBitesWidget);
        if (cardletQuickBitesWidget.getCardletId() == null) {
            throw new BadRequestAlertException("A new cardletQuickBitesWidget cannot already have an ID", ENTITY_NAME_QUICK_BITES);
        }
        CardletQuickBitesWidgetResponseVM result = cardletWidgetService.saveQuickBitesWidget(cardletQuickBitesWidget);
        return ResponseEntity.created(new URI("/api/cardlet-quick-bites-widgets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME_QUICK_BITES, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cardlet-quick-bites-widgets : Updates an existing cardletQuickBitesWidget.
     *
     * @param cardletQuickBitesWidget the cardletQuickBitesWidget to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cardletQuickBitesWidget,
     * or with status 400 (Bad Request) if the cardletQuickBitesWidget is not valid,
     * or with status 500 (Internal Server Error) if the cardletQuickBitesWidget couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cardlet-quick-bites-widgets")
    @Timed
    public ResponseEntity<CardletQuickBitesWidgetResponseVM> updateCardletQuickBitesWidget(@Valid @RequestBody CardletQuickBitesWidgetRequestVM cardletQuickBitesWidget) throws URISyntaxException {
        log.debug("REST request to update CardletQuickBitesWidget : {}", cardletQuickBitesWidget);
        if (cardletQuickBitesWidget.getId() == null) {
            return createCardletQuickBitesWidget(cardletQuickBitesWidget);
        }
        CardletQuickBitesWidgetResponseVM result = cardletWidgetService.saveQuickBitesWidget(cardletQuickBitesWidget);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME_QUICK_BITES, cardletQuickBitesWidget.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cardlet-quick-bites-widgets : get all the cardletQuickBitesWidgets.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of cardletQuickBitesWidgets in body
     */
    @GetMapping("/cardlet-quick-bites-widgets")
    @Timed
    public ResponseEntity<List<CardletQuickBitesWidgetResponseVM>> getAllCardletQuickBitesWidgets(Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get a page of CardletQuickBitesWidgets");
        Page<CardletQuickBitesWidgetResponseVM> page = cardletWidgetService.findAllCardletQuickBitesWidgetRepository(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cardlet-quick-bites-widgets");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /cardlet-quick-bites-widgets/:id : get the "id" cardletQuickBitesWidget.
     *
     * @param id the id of the cardletQuickBitesWidget to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cardletQuickBitesWidget, or with status 404 (Not Found)
     */
    @GetMapping("/cardlet-quick-bites-widgets/{id}")
    @Timed
    public ResponseEntity<CardletQuickBitesWidget> getCardletQuickBitesWidget(@PathVariable Long id) {
        log.debug("REST request to get CardletQuickBitesWidget : {}", id);
        CardletQuickBitesWidget cardletQuickBitesWidget = cardletQuickBitesWidgetRepository.findOne(id);
        return Optional.ofNullable(cardletQuickBitesWidget)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /cardlet-quick-bites-widgets/:id : delete the "id" cardletQuickBitesWidget.
     *
     * @param id the id of the cardletQuickBitesWidget to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cardlet-quick-bites-widgets/{id}")
    @Timed
    public ResponseEntity<Void> deleteCardletQuickBitesWidget(@PathVariable Long id) {
        log.debug("REST request to delete CardletQuickBitesWidget : {}", id);
        cardletQuickBitesWidgetRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME_QUICK_BITES, id.toString())).build();
    }
}
