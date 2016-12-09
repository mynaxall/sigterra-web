package itomy.sigterra.web.rest;

import com.codahale.metrics.annotation.Timed;
import itomy.sigterra.domain.Item;
import itomy.sigterra.domain.ItemData;

import itomy.sigterra.repository.ItemDataRepository;
import itomy.sigterra.repository.ItemRepository;
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
 * REST controller for managing ItemData.
 */
@RestController
@RequestMapping("/api")
public class ItemDataResource {

    private final Logger log = LoggerFactory.getLogger(ItemDataResource.class);

    @Inject
    private ItemDataRepository itemDataRepository;

    @Inject
    private ItemRepository itemRepository;

    /**
     * POST  /item-data : Create a new itemData.
     *
     * @param itemData the itemData to create
     * @return the ResponseEntity with status 201 (Created) and with body the new itemData, or with status 400 (Bad Request) if the itemData has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/item-data")
    @Timed
    public ResponseEntity<ItemData> createItemData(@RequestBody ItemData itemData) throws URISyntaxException {
        log.debug("REST request to save ItemData : {}", itemData);
        if (itemData.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("itemData", "idexists", "A new itemData cannot already have an ID")).body(null);
        }
        ItemData result = itemDataRepository.save(itemData);
        return ResponseEntity.created(new URI("/api/item-data/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("itemData", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /item-data : Updates an existing itemData.
     *
     * @param itemData the itemData to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated itemData,
     * or with status 400 (Bad Request) if the itemData is not valid,
     * or with status 500 (Internal Server Error) if the itemData couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/item-data")
    @Timed
    public ResponseEntity<ItemData> updateItemData(@RequestBody ItemData itemData) throws URISyntaxException {
        log.debug("REST request to update ItemData : {}", itemData);
        if (itemData.getId() == null) {
            return createItemData(itemData);
        }
        ItemData result = itemDataRepository.save(itemData);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("itemData", itemData.getId().toString()))
            .body(result);
    }

    /**
     * GET  /item-data : get all the itemData.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of itemData in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/item-data")
    @Timed
    public ResponseEntity<List<ItemData>> getAllItemData(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ItemData");
        Page<ItemData> page = itemDataRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/item-data");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /item-data/:id : get the "id" itemData.
     *
     * @param id the id of the itemData to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the itemData, or with status 404 (Not Found)
     */
    @GetMapping("/item-data/{id}")
    @Timed
    public ResponseEntity<ItemData> getItemData(@PathVariable Long id) {
        log.debug("REST request to get ItemData : {}", id);
        ItemData itemData = itemDataRepository.findOne(id);
        return Optional.ofNullable(itemData)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /item-data/:id : delete the "id" itemData.
     *
     * @param id the id of the itemData to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/item-data/{id}")
    @Timed
    public ResponseEntity<Void> deleteItemData(@PathVariable Long id) {
        log.debug("REST request to delete ItemData : {}", id);
        itemDataRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("itemData", id.toString())).build();
    }


    @GetMapping("/itemsData/caedlet/{id}")
    @Timed
    public ResponseEntity<List<ItemData>> getBusinessByCardlet(@PathVariable Long id) {
        Item item = itemRepository.findOne(id);
        List<ItemData> itemDatas = itemDataRepository.findAllByItem(item);
        return new ResponseEntity<>(itemDatas, HttpStatus.OK);
    }



    }
