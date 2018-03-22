package itomy.sigterra.web.rest;

import com.codahale.metrics.annotation.Timed;
import itomy.sigterra.annotation.Analytic;
import itomy.sigterra.domain.Cardlet;
import itomy.sigterra.domain.enumeration.EventType;
import itomy.sigterra.repository.BusinessRepository;
import itomy.sigterra.repository.CardletRepository;
import itomy.sigterra.service.AWSS3BucketService;
import itomy.sigterra.service.CardletService;
import itomy.sigterra.service.EventService;
import itomy.sigterra.service.UserService;
import itomy.sigterra.service.dto.UserCardletDTO;
import itomy.sigterra.web.rest.util.ConverterUtil;
import itomy.sigterra.web.rest.util.HeaderUtil;
import itomy.sigterra.web.rest.util.PaginationUtil;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


/**
 * REST controller for managing Cardlet.
 */
@RestController
@RequestMapping("/api")
public class CardletResource {

    public static final int MAX_ALLOWED_PROFILE_ICON_SIZE = 20 * 1024 * 1024;
    public static final int MAX_ALLOWED_PDF_SIZE = 50 * 1024 * 1024;

    public static final int MAX_NUMBER_OF_PDF_PAGES = 10;
    public static final String PDF_ITEM_NAME = "pdf-item";


    private final Logger log = LoggerFactory.getLogger(CardletResource.class);
    @Inject
    BusinessRepository businessRepository;
    @Inject
    private CardletRepository cardletRepository;
    @Inject
    private UserService userService;

    @Inject
    private CardletService cardletService;
    @Inject
    private EventService eventService;

    @Inject
    private AWSS3BucketService awss3BucketService;


    @PostMapping("cardlet/upload/presentation-pdf/{cardletId}")
    @Timed
    public ResponseEntity<?> uploadPresentationFile(@RequestParam("file") MultipartFile file, @RequestParam String cardletId) throws JSONException, IOException {
        JSONObject successObject = new JSONObject();
        if (file == null && file.isEmpty()) {
            successObject.put("success", false);
            successObject.put("message", "File is empty");

            return new ResponseEntity<>(successObject, HttpStatus.OK);
        }
        if (file.getSize() > MAX_ALLOWED_PDF_SIZE) {
            successObject.put("success", false);
            successObject.put("message", "File is too big. Max allowed file size is 50Mb");
            return ResponseEntity.ok(successObject);
        }

        File pdf = ConverterUtil.convertMultipartFileToJavaFile(file);
        PDDocument document = PDDocument.load(pdf);

        List<PDPage> listOfPages = document.getDocumentCatalog().getAllPages();

        if (listOfPages.size() > MAX_NUMBER_OF_PDF_PAGES) {
            listOfPages.subList(9, listOfPages.size() - 1).clear();
        }

        List<URI> listOfImages = convertPDFToImages(listOfPages, cardletId);

        document.close();

        if (listOfImages == null || listOfImages.isEmpty()) {
            successObject.put("success", false);
            successObject.put("message", "No response from server");
            return ResponseEntity.ok(successObject);
        }

        return new ResponseEntity<>(listOfImages, HttpStatus.OK);
    }

    private List<URI> convertPDFToImages(List<PDPage> listOfPages, String cardletId) throws IOException {
        List<URI> listOfURIImages = new ArrayList<>();
        int count = 1;
        for (PDPage page : listOfPages) {
            BufferedImage image = page.convertToImage();
            String name = PDF_ITEM_NAME + "-" + count++;
            File outPutFile = new File(".jpeg");
            ImageIO.write(image, "jpeg", outPutFile);
            MultipartFile multipartFile = ConverterUtil.convertJavaFileToMultipartFile(outPutFile);
            URI url = awss3BucketService.uploadSignatureImage(multipartFile, cardletId, name);
            listOfURIImages.add(url);
        }

        return listOfURIImages;
    }


    /**
     * POST  /cardlets : Create a new cardlet.
     *
     * @param cardlet the cardlet to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cardlet, or with status 400 (Bad Request) if the cardlet has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cardlets")
    @Timed
    public ResponseEntity<Cardlet> createCardlet(@RequestBody Cardlet cardlet) throws URISyntaxException {
        log.debug("REST request to save Cardlet : {}", cardlet);
        if (cardlet.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("cardlet", "idexists", "A new cardlet cannot already have an ID")).body(null);
        }
        Cardlet result = cardletRepository.save(cardlet);
        return ResponseEntity.created(new URI("/api/cardlets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("cardlet", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cardlets : Updates an existing cardlet.
     *
     * @param cardlet the cardlet to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cardlet,
     * or with status 400 (Bad Request) if the cardlet is not valid,
     * or with status 500 (Internal Server Error) if the cardlet couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cardlets")
    @Timed
    public ResponseEntity<Cardlet> updateCardlet(@RequestBody Cardlet cardlet) throws URISyntaxException {
        log.debug("REST request to update Cardlet : {}", cardlet);
        if (cardlet.getId() == null) {
            return createCardlet(cardlet);
        }
        Cardlet result = cardletRepository.save(cardlet);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("cardlet", cardlet.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cardlets : get all the cardlets.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of cardlets in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/cardlets")
    @Timed
    public ResponseEntity<List<?>> getAllCardlets(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Cardlets");
        Page<Cardlet> page = cardletRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cardlets");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /cardlets/:id : get the "id" cardlet.
     *
     * @param id the id of the cardlet to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cardlet, or with status 404 (Not Found)
     */
    @GetMapping("/cardlets/{id}")
    @Timed
    public ResponseEntity<Cardlet> getCardlet(@PathVariable Long id) {
        log.debug("REST request to get Cardlet : {}", id);
        Cardlet cardlet = cardletRepository.findOne(id);
        return Optional.ofNullable(cardlet)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /cardlets/:id : delete the "id" cardlet.
     *
     * @param id the id of the cardlet to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @GetMapping("/cardletDelete/{id}")
    @Timed
    public ResponseEntity<List<?>> deleteCardlet(@PathVariable Long id) {
        log.debug("REST request to delete Cardlet : {}", id);
        List<UserCardletDTO> usetCardletDTOs = cardletService.deleteCardlet(id);
        return new ResponseEntity<>(usetCardletDTOs, HttpStatus.OK);
    }

    @GetMapping("/userCardlets")
    @Timed
    public ResponseEntity<List<?>> getAllCardletsByUser() {


        List<UserCardletDTO> usetCardletDTOs = cardletService.userCardlets();
        log.debug("REST request to get a page of Cardlets");

        return new ResponseEntity<>(usetCardletDTOs, HttpStatus.OK);
    }

    @GetMapping("/userCardlet/{id}")
    @Timed
    @Analytic(type = EventType.VIEW)
    public ResponseEntity<?> getUserCardletById(@PathVariable Long id) {
        log.debug("REST request to get Cardlet : {}", id);
        UserCardletDTO usetCardletDTOs = cardletService.getCardlet(id, true);
        if (usetCardletDTOs == null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("cardlet", "idexists", "Access denied")).body(null);
        }
        eventService.viewEvent(id);
        return new ResponseEntity<>(usetCardletDTOs, HttpStatus.OK);
    }


    @GetMapping("/cardlet/{id}")
    @Timed
    @Analytic(type = EventType.VIEW)
    public ResponseEntity<?> getCardletById(@PathVariable Long id) {
        log.debug("REST request to get Cardlet : {}", id);
        UserCardletDTO usetCardletDTOs = cardletService.getCardlet(id, false);

        eventService.viewEvent(id);
        return new ResponseEntity<>(usetCardletDTOs, HttpStatus.OK);
    }

    @PostMapping(value = "/editCardlet", produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserCardletDTO> editCardlet(@RequestBody UserCardletDTO cardletDTO) {

        cardletService.createCardlet(cardletDTO, true, null, false, false);

        return new ResponseEntity<>(cardletDTO, HttpStatus.OK);
    }

    @PostMapping(value = "/cardlet/pdf/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> uploadPDF(@RequestBody MultipartFile file, @PathVariable String id) throws JSONException {
        if (file == null || file.isEmpty()) {
            JSONObject resp = new JSONObject();
            resp.put("success", false);
            resp.put("message", "File is empty");

            return new ResponseEntity<>(resp, HttpStatus.OK);
        }

        if (file.getSize() > MAX_ALLOWED_PDF_SIZE) {
            JSONObject resp = new JSONObject();
            resp.put("success", false);
            resp.put("message", "File is too big. Max allowed file size is 50Mb");

            return ResponseEntity.ok(resp);
        }
        String name = new SimpleDateFormat("yyyy-MM-dd-HH-mm-S").format(new Date());
        JSONObject successObject = cardletService.fileUploading(file, id, name, false);
        return new ResponseEntity<>(successObject.toString(), HttpStatus.OK);

    }

    @PostMapping(value = "/cardletFirst", produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserCardletDTO> createFirstCardlet(@RequestBody UserCardletDTO cardletDTO, @RequestParam(value = "id") Long id) {

        cardletService.createCardlet(cardletDTO, false, id, true, false);

        return new ResponseEntity<>(cardletDTO, HttpStatus.OK);
    }


    @PostMapping(value = "/cardlet", produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserCardletDTO> createCardlet(@RequestBody UserCardletDTO cardletDTO) {


        UserCardletDTO result = cardletService.createCardlet(cardletDTO, false, null, false, false);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/clone", produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserCardletDTO> cloneCardlet(@RequestBody UserCardletDTO cardletDTO) {

        cardletService.createCardlet(cardletDTO, false, null, false, true);

        return new ResponseEntity<>(cardletDTO, HttpStatus.OK);
    }

    @PostMapping(value = "cardlet/upload/icon/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> uploadProfileIcon(@RequestBody MultipartFile file, @PathVariable String id) throws JSONException {
        JSONObject successObject = cardletService.fileUploading(file, id, null, false);
        return new ResponseEntity<>(successObject.toString(), HttpStatus.OK);
    }


    @PostMapping(value = "signature/upload/icon/{id}/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> uploadSignatureIcon(@RequestBody MultipartFile file, @PathVariable String id, @PathVariable String name) throws JSONException {
        JSONObject successObject = cardletService.fileUploading(file, id, name, true);
        return new ResponseEntity<>(successObject.toString(), HttpStatus.OK);
    }


}
