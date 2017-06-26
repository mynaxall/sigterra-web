package itomy.sigterra.web.rest;

import com.codahale.metrics.annotation.Timed;
import itomy.sigterra.domain.AddressBook;

import itomy.sigterra.domain.Cardlet;
import itomy.sigterra.domain.User;
import itomy.sigterra.repository.AddressBookRepository;
import itomy.sigterra.repository.CardletRepository;
import itomy.sigterra.repository.UserRepository;
import itomy.sigterra.security.SecurityUtils;
import itomy.sigterra.service.CardletService;
import itomy.sigterra.service.dto.UserCardletDTO;
import itomy.sigterra.service.util.AddressBookService;
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
 * REST controller for managing AddressBook.
 */
@RestController
@RequestMapping("/api")
public class AddressBookResource {

    private final Logger log = LoggerFactory.getLogger(AddressBookResource.class);

    @Inject
    private AddressBookRepository addressBookRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private CardletRepository cardletRepository;

    @Inject
    private AddressBookService addressBookService;


    /**
     * POST  /address-books : Create a new addressBook.
     *
     * @param addressBook the addressBook to create
     * @return the ResponseEntity with status 201 (Created) and with body the new addressBook, or with status 400 (Bad Request) if the addressBook has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/address-books")
    @Timed
    public ResponseEntity<AddressBook> createAddressBook(@RequestBody AddressBook addressBook) throws URISyntaxException {
        log.debug("REST request to save AddressBook : {}", addressBook);
        if (addressBook.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("addressBook", "idexists", "A new addressBook cannot already have an ID")).body(null);
        }
        AddressBook result = addressBookRepository.save(addressBook);
        return ResponseEntity.created(new URI("/api/address-books/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("addressBook", result.getId().toString()))
            .body(result);
    }

    @PostMapping("/address-book")
    @Timed
    public ResponseEntity<AddressBook> createNewAddressBook(@RequestBody Long catdletId) throws URISyntaxException {
        AddressBook addressBook = new AddressBook();

        Optional<User> optionalUser = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
        User user = null;
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        }

        Cardlet cardlet = cardletRepository.findOne(catdletId);

        addressBook.setUser(user);
        addressBook.setCardlet(cardlet);


        AddressBook result = addressBookRepository.save(addressBook);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * PUT  /address-books : Updates an existing addressBook.
     *
     * @param addressBook the addressBook to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated addressBook,
     * or with status 400 (Bad Request) if the addressBook is not valid,
     * or with status 500 (Internal Server Error) if the addressBook couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/address-books")
    @Timed
    public ResponseEntity<AddressBook> updateAddressBook(@RequestBody AddressBook addressBook) throws URISyntaxException {
        log.debug("REST request to update AddressBook : {}", addressBook);
        if (addressBook.getId() == null) {
            return createAddressBook(addressBook);
        }
        AddressBook result = addressBookRepository.save(addressBook);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("addressBook", addressBook.getId().toString()))
            .body(result);
    }

    /**
     * GET  /address-books : get all the addressBooks.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of addressBooks in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/address-books")
    @Timed
    public ResponseEntity<List<AddressBook>> getAllAddressBooks(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of AddressBooks");
        Page<AddressBook> page = addressBookRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/address-books");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


    @GetMapping("/user-address-books")
    @Timed
    public ResponseEntity<List<?>> getUserAddressBooks()
    {
        log.debug("REST request to get a page of AddressBooks");
        List<UserCardletDTO> usetCardletDTOs = addressBookService.userCardlets();
        return new ResponseEntity<>(usetCardletDTOs, HttpStatus.OK);
    }



    /**
     * GET  /address-books/:id : get the "id" addressBook.
     *
     * @param id the id of the addressBook to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the addressBook, or with status 404 (Not Found)
     */
    @GetMapping("/address-books/{id}")
    @Timed
    public ResponseEntity<AddressBook> getAddressBook(@PathVariable Long id) {
        log.debug("REST request to get AddressBook : {}", id);
        AddressBook addressBook = addressBookRepository.findOne(id);
        return Optional.ofNullable(addressBook)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /address-books/:id : delete the "id" addressBook.
     *
     * @param id the id of the addressBook to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/address-books/{id}")
    @Timed
    public ResponseEntity<Void> deleteAddressBook(@PathVariable Long id) {
        log.debug("REST request to delete AddressBook : {}", id);
        addressBookRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("addressBook", id.toString())).build();
    }

}
