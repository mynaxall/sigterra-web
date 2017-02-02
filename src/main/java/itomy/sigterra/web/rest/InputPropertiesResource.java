package itomy.sigterra.web.rest;

import com.codahale.metrics.annotation.Timed;
import itomy.sigterra.domain.InputProperties;

import itomy.sigterra.repository.InputPropertiesRepository;
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
 * REST controller for managing InputProperties.
 */
@RestController
@RequestMapping("/api")
public class InputPropertiesResource {

    private final Logger log = LoggerFactory.getLogger(InputPropertiesResource.class);
        
    @Inject
    private InputPropertiesRepository inputPropertiesRepository;

    /**
     * POST  /input-properties : Create a new inputProperties.
     *
     * @param inputProperties the inputProperties to create
     * @return the ResponseEntity with status 201 (Created) and with body the new inputProperties, or with status 400 (Bad Request) if the inputProperties has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/input-properties")
    @Timed
    public ResponseEntity<InputProperties> createInputProperties(@RequestBody InputProperties inputProperties) throws URISyntaxException {
        log.debug("REST request to save InputProperties : {}", inputProperties);
        if (inputProperties.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("inputProperties", "idexists", "A new inputProperties cannot already have an ID")).body(null);
        }
        InputProperties result = inputPropertiesRepository.save(inputProperties);
        return ResponseEntity.created(new URI("/api/input-properties/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("inputProperties", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /input-properties : Updates an existing inputProperties.
     *
     * @param inputProperties the inputProperties to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated inputProperties,
     * or with status 400 (Bad Request) if the inputProperties is not valid,
     * or with status 500 (Internal Server Error) if the inputProperties couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/input-properties")
    @Timed
    public ResponseEntity<InputProperties> updateInputProperties(@RequestBody InputProperties inputProperties) throws URISyntaxException {
        log.debug("REST request to update InputProperties : {}", inputProperties);
        if (inputProperties.getId() == null) {
            return createInputProperties(inputProperties);
        }
        InputProperties result = inputPropertiesRepository.save(inputProperties);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("inputProperties", inputProperties.getId().toString()))
            .body(result);
    }

    /**
     * GET  /input-properties : get all the inputProperties.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of inputProperties in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/input-properties")
    @Timed
    public ResponseEntity<List<InputProperties>> getAllInputProperties(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of InputProperties");
        Page<InputProperties> page = inputPropertiesRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/input-properties");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /input-properties/:id : get the "id" inputProperties.
     *
     * @param id the id of the inputProperties to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the inputProperties, or with status 404 (Not Found)
     */
    @GetMapping("/input-properties/{id}")
    @Timed
    public ResponseEntity<InputProperties> getInputProperties(@PathVariable Long id) {
        log.debug("REST request to get InputProperties : {}", id);
        InputProperties inputProperties = inputPropertiesRepository.findOne(id);
        return Optional.ofNullable(inputProperties)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /input-properties/:id : delete the "id" inputProperties.
     *
     * @param id the id of the inputProperties to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/input-properties/{id}")
    @Timed
    public ResponseEntity<Void> deleteInputProperties(@PathVariable Long id) {
        log.debug("REST request to delete InputProperties : {}", id);
        inputPropertiesRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("inputProperties", id.toString())).build();
    }

}
