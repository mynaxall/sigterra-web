package itomy.sigterra.web.rest;

import com.codahale.metrics.annotation.Timed;
import itomy.sigterra.domain.Signature;

import itomy.sigterra.repository.SignatureRepository;
import itomy.sigterra.web.rest.util.HeaderUtil;
import itomy.sigterra.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Signature.
 */
@RestController
@RequestMapping("/api")
public class SignatureResource {

    private final Logger log = LoggerFactory.getLogger(SignatureResource.class);
        
    @Inject
    private SignatureRepository signatureRepository;

    /**
     * POST  /signatures : Create a new signature.
     *
     * @param signature the signature to create
     * @return the ResponseEntity with status 201 (Created) and with body the new signature, or with status 400 (Bad Request) if the signature has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/signatures")
    @Timed
    public ResponseEntity<Signature> createSignature(@RequestBody Signature signature) throws URISyntaxException {
        log.debug("REST request to save Signature : {}", signature);
        if (signature.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("signature", "idexists", "A new signature cannot already have an ID")).body(null);
        }
        Signature result = signatureRepository.save(signature);
        return ResponseEntity.created(new URI("/api/signatures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("signature", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /signatures : Updates an existing signature.
     *
     * @param signature the signature to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated signature,
     * or with status 400 (Bad Request) if the signature is not valid,
     * or with status 500 (Internal Server Error) if the signature couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/signatures")
    @Timed
    public ResponseEntity<Signature> updateSignature(@RequestBody Signature signature) throws URISyntaxException {
        log.debug("REST request to update Signature : {}", signature);
        if (signature.getId() == null) {
            return createSignature(signature);
        }
        Signature result = signatureRepository.save(signature);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("signature", signature.getId().toString()))
            .body(result);
    }

    /**
     * GET  /signatures : get all the signatures.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of signatures in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/signatures")
    @Timed
    public ResponseEntity<List<Signature>> getAllSignatures(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Signatures");
        Page<Signature> page = signatureRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/signatures");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /signatures/:id : get the "id" signature.
     *
     * @param id the id of the signature to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the signature, or with status 404 (Not Found)
     */
    @GetMapping("/signatures/{id}")
    @Timed
    public ResponseEntity<Signature> getSignature(@PathVariable Long id) {
        log.debug("REST request to get Signature : {}", id);
        Signature signature = signatureRepository.findOne(id);
        return Optional.ofNullable(signature)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /signatures/:id : delete the "id" signature.
     *
     * @param id the id of the signature to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/signatures/{id}")
    @Timed
    public ResponseEntity<Void> deleteSignature(@PathVariable Long id) {
        log.debug("REST request to delete Signature : {}", id);
        signatureRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("signature", id.toString())).build();
    }

}
