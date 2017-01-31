package itomy.sigterra.web.rest;

import com.codahale.metrics.annotation.Timed;
import itomy.sigterra.domain.*;

import itomy.sigterra.repository.CardletRepository;
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
import java.util.*;

/**
 * REST controller for managing Cardlet.
 */
@RestController
@RequestMapping("/api")
public class CardletResource {

    private final Logger log = LoggerFactory.getLogger(CardletResource.class);

    @Inject
    private CardletRepository cardletRepository;

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
    @DeleteMapping("/cardlets/{id}")
    @Timed
    public ResponseEntity<Void> deleteCardlet(@PathVariable Long id) {
        log.debug("REST request to delete Cardlet : {}", id);
        cardletRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("cardlet", id.toString())).build();
    }

    @GetMapping("/userCardlets")
    @Timed
    public ResponseEntity<List<?>> getAllCardletsByUser(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Cardlets");
        List<Cardlet> cardlets = cardletRepository.findByUserIsCurrentUser();
        List<CardletDTO> cardletDTOs = new LinkedList<>();
        Set<ItemDTO> itemDTOs = new HashSet<>();
        Set<BusinessDTO> businessDTOs = new HashSet<>();
        for (Cardlet cardlet : cardlets) {
            Set<Item> items = cardlet.getItems();
            Set<Business> businesses = cardlet.getBusinesses();
            for (Business business : businesses) {
                BusinessDTO businessDTO = new BusinessDTO(business);
                businessDTOs.add(businessDTO);
            }
            for (Item item : items) {
                Set<ItemData> itemDatas = item.getItemData();
                Set<ItemDataDTO> itemDataDTOs = new HashSet<>();
                for (ItemData itemData : itemDatas) {
                    ItemDataDTO itemDataDTO = new ItemDataDTO(itemData);
                    itemDataDTOs.add(itemDataDTO);
                }

                ItemDTO itemDTO = new ItemDTO(item.getId(),item.getName(),item.getIcon(),item.getCreatedDate(),item.getModifiDate(),item.getMainColor(),item.getColor(),
                    item.getTabType(), itemDataDTOs);
                itemDTOs.add(itemDTO);
            }
            CardletDTO cardletDTO = new CardletDTO(cardlet.getId(), cardlet.getName(), cardlet.getCreatedDate(),cardlet.getModifiedDate(), cardlet.isActive()
            ,cardlet.getUser(), businessDTOs, itemDTOs);
            cardletDTOs.add(cardletDTO);


        }
        return new ResponseEntity<>(cardletDTOs, HttpStatus.OK);
    }


}
