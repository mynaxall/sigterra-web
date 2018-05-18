package itomy.sigterra.web.rest;

import com.codahale.metrics.annotation.Timed;
import itomy.sigterra.domain.Cardlet;
import itomy.sigterra.domain.CardletContentLibraryWidget;
import itomy.sigterra.repository.CardletContentLibraryWidgetRepository;
import itomy.sigterra.repository.CardletQuickBitesWidgetRepository;
import itomy.sigterra.repository.CardletRepository;
import itomy.sigterra.repository.CardletTestimonialWidgetRepository;
import itomy.sigterra.service.CardletWidgetService;
import itomy.sigterra.service.ContentLibraryWidgetService;
import itomy.sigterra.web.rest.errors.BadRequestAlertException;
import itomy.sigterra.web.rest.util.HeaderUtil;
import itomy.sigterra.web.rest.vm.CardletImagesContentLibraryResponseVM;
import itomy.sigterra.web.rest.vm.CardletWidgetRequestVM;
import itomy.sigterra.web.rest.vm.CardletWidgetResponseVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.Optional;

import static itomy.sigterra.service.util.RequestUtil.getRequestIp;
import static itomy.sigterra.service.util.RequestUtil.getRequestUserAgent;
import static itomy.sigterra.web.rest.util.ResponseUtil.errorResponse;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * REST controller for managing CardletTestimonialWidget.
 */
@RestController
@RequestMapping("/api")
public class CardletWidgetResource {

    private final Logger log = LoggerFactory.getLogger(CardletWidgetResource.class);

    private static final String ENTITY_NAME_TESTIOMONIAL = "cardletTestimonialWidget";
    private static final String ENTITY_NAME_QUICK_BITES = "cardletQuickBitesWidget";
    private static final String ENTITY_CONTENT_LIBRARY = "cardletContentLibraryWidget";

    @Inject
    private CardletRepository cardletRepository;

    @Inject
    private CardletTestimonialWidgetRepository cardletTestimonialWidgetRepository;

    @Inject
    private CardletQuickBitesWidgetRepository cardletQuickBitesWidgetRepository;

    @Inject
    private CardletWidgetService cardletWidgetService;

    @Inject
    private CardletContentLibraryWidgetRepository cardletContentLibraryWidgetRepository;

    @Inject
    private ContentLibraryWidgetService widgetLikesService;


    /**
     * PUT  /cardlet-testimonial-widgets : Updates an existing cardletTestimonialWidget.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the updated cardletTestimonialWidget,
     * or with status 400 (Bad Request) if the cardletTestimonialWidget is not valid,
     * or with status 500 (Internal Server Error) if the cardletTestimonialWidget couldn't be updated
     */
    @PutMapping("/cardlet-widgets")
    @Timed
    public ResponseEntity<CardletWidgetResponseVM> updateCardletTestimonialWidget(@Valid @RequestBody CardletWidgetRequestVM widget) {
        log.debug("REST request to update CardletTestimonialWidget : {}", widget);

        CardletWidgetResponseVM result = cardletWidgetService.saveCardletlWidgetes(widget);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME_TESTIOMONIAL, result.getCardletId().toString()))
            .body(result);
    }

    /**
     * GET  /cardlet/{cardletId}/cardlet-widgets : get all the cardletTestimonialWidgets.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cardletTestimonialWidgets in body
     */
    @GetMapping("/cardlet/{cardletId}/widgets")
    @Timed
    public ResponseEntity<CardletWidgetResponseVM> getAllCardletTestimonialWidgets(@PathVariable Long cardletId) {
        log.debug("REST request to get a page of CardletTestimonialWidgets");

        CardletWidgetResponseVM cardletWidgetes = cardletWidgetService.getCardletWidgetesByCardletId(cardletId);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME_TESTIOMONIAL, cardletId.toString()))
            .body(cardletWidgetes);
    }

    @PostMapping("/cardlet/{cardletId:\\d+}/like/content-library-widget/{widgetId:\\d+}")
    public ResponseEntity likeContentLibraryWidget(@PathVariable Long cardletId, @PathVariable Long widgetId) {
        log.debug("REST request to like content library widget with id = {}, from user with ip = {} and agent = {}", widgetId, getRequestIp(), getRequestUserAgent());

        Cardlet cardlet = cardletRepository.findOne(cardletId);
        if (cardlet == null) {
            return errorResponse(HttpStatus.NOT_FOUND, "Cardlet not found");
        }

        CardletContentLibraryWidget widget = cardletContentLibraryWidgetRepository.findByIdAndCardletId(widgetId, cardletId);
        if (widget == null) {
            return errorResponse(HttpStatus.NOT_FOUND, "Content library widget not found");
        }
        try {

            widgetLikesService.likeCardletContentLibraryWidget(cardlet, widget);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException ex) {
            return errorResponse(BAD_REQUEST, ex.getMessage());
        }
    }

    @PostMapping("/cardlet/{cardletId:\\d+}/view/content-library-widget/{widgetId:\\d+}")
    public ResponseEntity viewContentLibraryWidget(@PathVariable Long cardletId, @PathVariable Long widgetId) {
        log.debug("REST request to view content library widget with id = {}, from user with ip = {} and agent = {}", widgetId, getRequestIp(), getRequestUserAgent());

        Cardlet cardlet = cardletRepository.findOne(cardletId);
        if (cardlet == null) {
            return errorResponse(HttpStatus.NOT_FOUND, "Cardlet not found");
        }
        CardletContentLibraryWidget widget = cardletContentLibraryWidgetRepository.findByIdAndCardletId(widgetId, cardletId);

        if (widget == null) {
            return errorResponse(HttpStatus.NOT_FOUND, "Content library widget not found");
        }
        try {

            widgetLikesService.viewCardletContentLibraryWidget(cardlet, widget);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException ex) {
            return errorResponse(BAD_REQUEST, ex.getMessage());
        }
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

    /**
     * DELETE  /cardlet-content-library-widgets/:id : delete the "id" cardletContentLibraryWidget.
     *
     * @param id the id of the cardletContentLibraryWidget to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cardlet-content-library-widgets/{id}")
    @Timed
    public ResponseEntity<Void> deleteCardletContentLibraryWidget(@PathVariable Long id) {
        log.debug("REST request to delete CardletContentLibraryWidget : {}", id);
        cardletWidgetService.deleteContentLibrary(id);
        cardletContentLibraryWidgetRepository.deleteById(id);

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityDeletionAlert(ENTITY_CONTENT_LIBRARY, id.toString()))
            .build();
    }

    @PostMapping("/cardlet/{cardletId}/widgets/images/upload")
    @Timed
    public ResponseEntity<CardletImagesContentLibraryResponseVM> uploadCardletContentLibraryImages(@PathVariable Long cardletId,
                                                                                                   @RequestPart(required = false) MultipartFile coverImage,
                                                                                                   @RequestPart(required = false) MultipartFile uploadFile) {
        log.debug("REST request to upload CardletContentLibraryWidget images. Cardlet ID : {}", cardletId);
        Cardlet cardlet = cardletRepository.findOne(cardletId);
        if (cardlet == null) {
            throw new BadRequestAlertException(ENTITY_CONTENT_LIBRARY, "Incorrect cardlet ID");
        }
        CardletImagesContentLibraryResponseVM uploadVM = new CardletImagesContentLibraryResponseVM();
        CardletImagesContentLibraryResponseVM widget = cardletWidgetService.uploadImages(uploadVM, cardlet.getId(), coverImage, uploadFile);

        return Optional.ofNullable(widget)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
