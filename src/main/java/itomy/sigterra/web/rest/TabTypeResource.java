package itomy.sigterra.web.rest;

import com.codahale.metrics.annotation.Timed;
import itomy.sigterra.domain.TabType;

import itomy.sigterra.repository.TabTypeRepository;
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
 * REST controller for managing TabType.
 */
@RestController
@RequestMapping("/api")
public class TabTypeResource {

    private final Logger log = LoggerFactory.getLogger(TabTypeResource.class);
        
    @Inject
    private TabTypeRepository tabTypeRepository;

    /**
     * POST  /tab-types : Create a new tabType.
     *
     * @param tabType the tabType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tabType, or with status 400 (Bad Request) if the tabType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tab-types")
    @Timed
    public ResponseEntity<TabType> createTabType(@RequestBody TabType tabType) throws URISyntaxException {
        log.debug("REST request to save TabType : {}", tabType);
        if (tabType.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("tabType", "idexists", "A new tabType cannot already have an ID")).body(null);
        }
        TabType result = tabTypeRepository.save(tabType);
        return ResponseEntity.created(new URI("/api/tab-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("tabType", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tab-types : Updates an existing tabType.
     *
     * @param tabType the tabType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tabType,
     * or with status 400 (Bad Request) if the tabType is not valid,
     * or with status 500 (Internal Server Error) if the tabType couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/tab-types")
    @Timed
    public ResponseEntity<TabType> updateTabType(@RequestBody TabType tabType) throws URISyntaxException {
        log.debug("REST request to update TabType : {}", tabType);
        if (tabType.getId() == null) {
            return createTabType(tabType);
        }
        TabType result = tabTypeRepository.save(tabType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("tabType", tabType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tab-types : get all the tabTypes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of tabTypes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/tab-types")
    @Timed
    public ResponseEntity<List<TabType>> getAllTabTypes(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of TabTypes");
        Page<TabType> page = tabTypeRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tab-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /tab-types/:id : get the "id" tabType.
     *
     * @param id the id of the tabType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tabType, or with status 404 (Not Found)
     */
    @GetMapping("/tab-types/{id}")
    @Timed
    public ResponseEntity<TabType> getTabType(@PathVariable Long id) {
        log.debug("REST request to get TabType : {}", id);
        TabType tabType = tabTypeRepository.findOne(id);
        return Optional.ofNullable(tabType)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /tab-types/:id : delete the "id" tabType.
     *
     * @param id the id of the tabType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tab-types/{id}")
    @Timed
    public ResponseEntity<Void> deleteTabType(@PathVariable Long id) {
        log.debug("REST request to delete TabType : {}", id);
        tabTypeRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("tabType", id.toString())).build();
    }

}
