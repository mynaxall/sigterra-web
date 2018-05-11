package itomy.sigterra.web.rest;

import com.codahale.metrics.annotation.Timed;
import itomy.sigterra.domain.Cardlet;
import itomy.sigterra.domain.CardletContentLibraryWidget;
import itomy.sigterra.repository.CardletContentLibraryWidgetRepository;
import itomy.sigterra.repository.CardletRepository;
import itomy.sigterra.service.CardletWidgetService;
import itomy.sigterra.service.mapper.CardletWidgetMapper;
import itomy.sigterra.web.rest.errors.BadRequestAlertException;
import itomy.sigterra.web.rest.util.HeaderUtil;
import itomy.sigterra.web.rest.util.PaginationUtil;
import itomy.sigterra.web.rest.vm.CardletContentLibraryWidgetRequestVM;
import itomy.sigterra.web.rest.vm.CardletContentLibraryWidgetResponseVM;
import itomy.sigterra.web.rest.vm.CardletImagesContentLibraryResponseVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing CardletContentLibraryWidget.
 */
@RestController
@RequestMapping("/api")
public class CardletContentLibraryWidgetResource {

    private final Logger log = LoggerFactory.getLogger(CardletContentLibraryWidgetResource.class);

    private static final String ENTITY_NAME = "cardletContentLibraryWidget";
    @Inject
    private CardletRepository cardletRepository;
    @Inject
    private final CardletContentLibraryWidgetRepository cardletContentLibraryWidgetRepository;

    @Inject
    private CardletWidgetService cardletWidgetService;

    public CardletContentLibraryWidgetResource(CardletContentLibraryWidgetRepository cardletContentLibraryWidgetRepository) {
        this.cardletContentLibraryWidgetRepository = cardletContentLibraryWidgetRepository;
    }

    /**
     * POST  /cardlet-content-library-widgets : Create a new cardletContentLibraryWidget.
     *
     * @return the ResponseEntity with status 201 (Created) and with body the new cardletContentLibraryWidget, or with status 400 (Bad Request) if the cardletContentLibraryWidget has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/cardlet-content-library-widgets", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<CardletContentLibraryWidgetResponseVM> createCardletContentLibraryWidget(@Valid @RequestBody CardletContentLibraryWidgetRequestVM cardletContentLibraryWidget) throws URISyntaxException {
        cardletContentLibraryWidget.setId(0L);
        log.debug("REST request to save CardletContentLibraryWidget : {}", cardletContentLibraryWidget);
        Cardlet cardlet = cardletRepository.findOne(cardletContentLibraryWidget.getCardletId());

        if (cardlet == null) {
            throw new BadRequestAlertException(ENTITY_NAME, "A new cardletContentLibraryWidget cannot already have an ID");
        }
        if (cardletContentLibraryWidget.getCoverImageUrl() == null || !cardletWidgetService.isWebLink(cardletContentLibraryWidget.getCoverImageUrl())) {
            throw new BadRequestAlertException(ENTITY_NAME, "Incorrect Cover image url");
        }
        if (cardletContentLibraryWidget.getUploadFileUrl() != null && !cardletWidgetService.isWebLink(cardletContentLibraryWidget.getUploadFileUrl())) {
            throw new BadRequestAlertException(ENTITY_NAME, "Incorrect upload file url");
        }

        CardletContentLibraryWidget contentLibraryWidget = CardletWidgetMapper.mapToEntity(cardletContentLibraryWidget);
        contentLibraryWidget.setCardlet(cardlet);

        CardletContentLibraryWidgetResponseVM result = new CardletContentLibraryWidgetResponseVM(cardletContentLibraryWidgetRepository
            .save(contentLibraryWidget));
        return ResponseEntity.created(new URI("/api/cardlet-content-library-widgets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cardlet-content-library-widgets : Updates an existing cardletContentLibraryWidget.
     *
     * @param cardletContentLibraryWidget the cardletContentLibraryWidget to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cardletContentLibraryWidget,
     * or with status 400 (Bad Request) if the cardletContentLibraryWidget is not valid,
     * or with status 500 (Internal Server Error) if the cardletContentLibraryWidget couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cardlet-content-library-widgets")
    @Timed
    public ResponseEntity<CardletContentLibraryWidgetResponseVM> updateCardletContentLibraryWidget(@Valid @RequestBody CardletContentLibraryWidgetRequestVM cardletContentLibraryWidget) throws URISyntaxException {

        log.debug("REST request to update CardletContentLibraryWidget : {}", cardletContentLibraryWidget);
        if (cardletContentLibraryWidget.getId() == null) {
            return createCardletContentLibraryWidget(cardletContentLibraryWidget);
        }

        CardletContentLibraryWidget contentLibraryWidget = CardletWidgetMapper.mapToEntity(cardletContentLibraryWidget);
        CardletContentLibraryWidget result = cardletWidgetService.mergeCardletContentLibraryWidget(contentLibraryWidget);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cardletContentLibraryWidget.getId().toString()))
            .body(new CardletContentLibraryWidgetResponseVM(result));
    }

    /**
     * GET  /cardlet-content-library-widgets : get all the cardletContentLibraryWidgets.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of cardletContentLibraryWidgets in body
     */
    @GetMapping("/cardlet/{cardletId}/cardlet-content-library-widgets")
    @Timed
    public ResponseEntity<List<CardletContentLibraryWidgetResponseVM>> getAllCardletContentLibraryWidgets(@PathVariable Long cardletId, Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get a page of CardletContentLibraryWidgets");
        Page<CardletContentLibraryWidgetResponseVM> page = cardletWidgetService.findAllContentLibraryByCardletId(cardletId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cardlet-content-library-widgets");

        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /cardlet-content-library-widgets/:id : get the "id" cardletContentLibraryWidget.
     *
     * @param id the id of the cardletContentLibraryWidget to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cardletContentLibraryWidget, or with status 404 (Not Found)
     */
    @GetMapping("/cardlet/{cardletId}/cardlet-content-library-widgets/{id}")
    @Timed
    public ResponseEntity<CardletContentLibraryWidgetResponseVM> getCardletContentLibraryWidget(@PathVariable Long cardletId, @PathVariable Long id) {
        log.debug("REST request to get CardletContentLibraryWidget : {}", id);
        CardletContentLibraryWidgetResponseVM cardletContentLibraryWidget = new CardletContentLibraryWidgetResponseVM(cardletContentLibraryWidgetRepository.findByIdAndCardletId(id, cardletId));

        return Optional.ofNullable(cardletContentLibraryWidget)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
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

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/cardlet/{cardletId}/cardlet-content-library-widgets/upload")
    @Timed
    public ResponseEntity<CardletImagesContentLibraryResponseVM> uploadCardletContentLibraryImages(@PathVariable Long cardletId,
                                                                                                   @RequestPart(required = false) MultipartFile coverImage,
                                                                                                   @RequestPart(required = false) MultipartFile uploadFile) {
        log.debug("REST request to upload CardletContentLibraryWidget images. Cardlet ID : {}", cardletId);
        Cardlet cardlet = cardletRepository.findOne(cardletId);
        if (cardlet == null) {
            throw new BadRequestAlertException(ENTITY_NAME, "Incorrect cardlet ID");
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
