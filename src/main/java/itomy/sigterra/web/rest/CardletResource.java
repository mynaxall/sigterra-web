package itomy.sigterra.web.rest;

import com.codahale.metrics.annotation.Timed;
import itomy.sigterra.domain.*;

import itomy.sigterra.repository.BusinessRepository;
import itomy.sigterra.repository.CardletRepository;
import itomy.sigterra.service.AWSS3BucketService;
import itomy.sigterra.service.CardletService;
import itomy.sigterra.service.UserService;
import itomy.sigterra.service.dto.CardletItemTab;
import itomy.sigterra.service.dto.CardletTab;
import itomy.sigterra.service.dto.ItemModel;
import itomy.sigterra.service.dto.UserCardletDTO;
import itomy.sigterra.web.rest.util.HeaderUtil;
import itomy.sigterra.web.rest.util.PaginationUtil;
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

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * REST controller for managing Cardlet.
 */
@RestController
@RequestMapping("/api")
public class CardletResource {

    public static final int MAX_ALLOWED_PROFILE_ICON_SIZE = 50 * 1024 * 1024;

    private final Logger log = LoggerFactory.getLogger(CardletResource.class);

    @Inject
    private CardletRepository cardletRepository;

    @Inject
    BusinessRepository businessRepository;

    @Inject
    private UserService userService;

    @Inject
    private CardletService cardletService;

    @Inject
    private AWSS3BucketService awss3BucketService;


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
    @GetMapping("/cardlets" )
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
    public ResponseEntity<List<?>> getAllCardletsByUser()
        throws URISyntaxException {


        List<UserCardletDTO> usetCardletDTOs = cardletService.userCardlets();
        log.debug("REST request to get a page of Cardlets");

        return new ResponseEntity<>(usetCardletDTOs, HttpStatus.OK);
    }


    @GetMapping("/cardlet/{id}")
    @Timed
    public ResponseEntity<?> getCardletById(@PathVariable Long id) {
        log.debug("REST request to get Cardlet : {}", id);
        UserCardletDTO usetCardletDTOs = cardletService.getCardlet(id);
        return new ResponseEntity<>(usetCardletDTOs, HttpStatus.OK);
    }

    @PostMapping(value = "/editCardlet", produces=MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserCardletDTO> editCardlet(@RequestBody UserCardletDTO cardletDTO)
        throws URISyntaxException {


        cardletService.createCardlet(cardletDTO, true);

        return new ResponseEntity<>(cardletDTO, HttpStatus.OK);
    }




    @PostMapping(value = "/cardlet", produces=MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserCardletDTO> createCardlet(@RequestBody UserCardletDTO cardletDTO)
        throws URISyntaxException {


        cardletService.createCardlet(cardletDTO, false);

        return new ResponseEntity<>(cardletDTO, HttpStatus.OK);
    }

    @PostMapping(value = "cardlet/upload/icon/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> uploadProfileIcon(@RequestBody MultipartFile file, @PathVariable String id) throws JSONException {
        log.info("asdasdasd========== "+id);

        JSONObject successObject = new JSONObject();
        if(file != null && !file.isEmpty()) {
            if(file.getSize() > MAX_ALLOWED_PROFILE_ICON_SIZE) {
                successObject.put("success", false);
                successObject.put("message", "File is too big. Max allowed file size is 50Mb");
                // TODO: 1/9/17 Maybe need to change it to not OK status
                return ResponseEntity.ok(successObject);
            }

            URI url = awss3BucketService.uploadBusinessImage(file, id);
            if(url == null) {
                successObject.put("success", false);
                successObject.put("message", "Unable to fetch the file from S3 bucket.");
            } else {
                successObject.put("success", true);
                successObject.put("url", url);
            }
        } else {
            successObject.put("success", false);
            successObject.put("message", "File is empty or NULL");
            // TODO: 1/9/17 Maybe need to change it to not OK status
        }
        return new ResponseEntity<>(successObject.toString(), HttpStatus.OK);
    }


}
