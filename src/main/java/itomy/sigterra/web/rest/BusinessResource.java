package itomy.sigterra.web.rest;

import com.codahale.metrics.annotation.Timed;
import itomy.sigterra.domain.Business;

import itomy.sigterra.repository.BusinessRepository;
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
 * REST controller for managing Business.
 */
@RestController
@RequestMapping("/api")
public class BusinessResource {

    private final Logger log = LoggerFactory.getLogger(BusinessResource.class);
        
    @Inject
    private BusinessRepository businessRepository;

    /**
     * POST  /businesses : Create a new business.
     *
     * @param business the business to create
     * @return the ResponseEntity with status 201 (Created) and with body the new business, or with status 400 (Bad Request) if the business has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/businesses")
    @Timed
    public ResponseEntity<Business> createBusiness(@RequestBody Business business) throws URISyntaxException {
        log.debug("REST request to save Business : {}", business);
        if (business.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("business", "idexists", "A new business cannot already have an ID")).body(null);
        }
        Business result = businessRepository.save(business);
        return ResponseEntity.created(new URI("/api/businesses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("business", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /businesses : Updates an existing business.
     *
     * @param business the business to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated business,
     * or with status 400 (Bad Request) if the business is not valid,
     * or with status 500 (Internal Server Error) if the business couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/businesses")
    @Timed
    public ResponseEntity<Business> updateBusiness(@RequestBody Business business) throws URISyntaxException {
        log.debug("REST request to update Business : {}", business);
        if (business.getId() == null) {
            return createBusiness(business);
        }
        Business result = businessRepository.save(business);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("business", business.getId().toString()))
            .body(result);
    }

    /**
     * GET  /businesses : get all the businesses.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of businesses in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/businesses")
    @Timed
    public ResponseEntity<List<Business>> getAllBusinesses(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Businesses");
        Page<Business> page = businessRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/businesses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /businesses/:id : get the "id" business.
     *
     * @param id the id of the business to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the business, or with status 404 (Not Found)
     */
    @GetMapping("/businesses/{id}")
    @Timed
    public ResponseEntity<Business> getBusiness(@PathVariable Long id) {
        log.debug("REST request to get Business : {}", id);
        Business business = businessRepository.findOne(id);
        return Optional.ofNullable(business)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /businesses/:id : delete the "id" business.
     *
     * @param id the id of the business to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/businesses/{id}")
    @Timed
    public ResponseEntity<Void> deleteBusiness(@PathVariable Long id) {
        log.debug("REST request to delete Business : {}", id);
        businessRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("business", id.toString())).build();
    }

}
