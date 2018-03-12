package itomy.sigterra.web.rest;


import com.codahale.metrics.annotation.Timed;
import itomy.sigterra.domain.enumeration.CardletFooterIndex;
import itomy.sigterra.service.CardletViewService;
import itomy.sigterra.web.rest.vm.CardletViewRequestResponseVM;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/cardlet/pageview/")
public class CardletViewResource {
    private final Logger log = LoggerFactory.getLogger(CardletViewResource.class);

    @Inject
    CardletViewService cardletViewService;

    @GetMapping(value = "{cardletId}",produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CardletViewRequestResponseVM> getCardletViewPageData(@PathVariable Long cardletId){
        log.debug("REST request to get Cardlet View: {}", cardletId);
        return new ResponseEntity<>(cardletViewService.getCardletView(cardletId), HttpStatus.OK);
    }


    /**
     * POST  create new page view
     *
     * @param cardletView the cardletView to create
     * @return the ResponseEntity with status 201 (Created) and with body the new CardletViewRequestResponseVM
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CardletViewRequestResponseVM> createPageView(@RequestBody CardletViewRequestResponseVM cardletView) throws URISyntaxException {
        log.debug("REST request to create new CardletView : {}", cardletView);
        CardletViewRequestResponseVM result = cardletViewService.saveCardletView(cardletView);
        return ResponseEntity.created(new URI("/api/cardlet/pageview/"+result.getCardletId()))
            .body(result);
    }

    /**
     * POST  update  page view
     *
     * @param cardletView the cardletView to create
     * @return the ResponseEntity with status 200 (OK) and with body the updated CardletViewRequestResponseVM
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CardletViewRequestResponseVM> updatePageView(@RequestBody CardletViewRequestResponseVM cardletView) throws URISyntaxException {
        log.debug("REST request to save new CardletView : {}", cardletView);
        CardletViewRequestResponseVM result = cardletViewService.saveCardletView(cardletView);
        return ResponseEntity.ok()
            .body(result);
    }

    @PostMapping(value = "logo-image/{cardletId}", produces=MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> uploadLogo(@RequestBody MultipartFile file, @PathVariable Long cardletId) throws JSONException {
        JSONObject successObject = cardletViewService.uploadLogo(file,cardletId);
        return new ResponseEntity<>(successObject.toString(), HttpStatus.OK);
    }

    @PostMapping(value = "photo-image/{cardletId}", produces=MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> uploadPhoto(@RequestBody MultipartFile file, @PathVariable Long cardletId) throws JSONException {
        JSONObject successObject = cardletViewService.uploadPhoto(file,cardletId);
        return new ResponseEntity<>(successObject.toString(), HttpStatus.OK);
    }

    @PostMapping(value = "background-image/{cardletId}", produces=MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> uploadBackground(@RequestBody MultipartFile file, @PathVariable Long cardletId) throws JSONException {
        JSONObject successObject = cardletViewService.uploadLogo(file,cardletId);
        return new ResponseEntity<>(successObject.toString(), HttpStatus.OK);
    }

    @PostMapping(value = "link-image/{cardletId}/{index}", produces=MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> uploadLinkImage(@RequestBody MultipartFile file, @PathVariable Long cardletId,@PathVariable CardletFooterIndex index) throws JSONException {
        JSONObject successObject = cardletViewService.uploadLinkLogo(file,cardletId,index.ordinal());
        return new ResponseEntity<>(successObject.toString(), HttpStatus.OK);
    }

}
