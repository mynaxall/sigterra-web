package itomy.sigterra.web.rest;

import com.codahale.metrics.annotation.Timed;
import itomy.sigterra.annotation.Analytic;
import itomy.sigterra.domain.AddressBook;
import itomy.sigterra.domain.Cardlet;
import itomy.sigterra.domain.User;
import itomy.sigterra.domain.enumeration.EventType;
import itomy.sigterra.repository.AddressBookRepository;
import itomy.sigterra.repository.CardletRepository;
import itomy.sigterra.repository.UserRepository;
import itomy.sigterra.security.SecurityUtils;
import itomy.sigterra.service.EventService;
import itomy.sigterra.service.dto.UserCardletDTO;
import itomy.sigterra.service.util.AddressBookService;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
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

    @Inject
    private EventService eventService;

    @PostMapping(path = "/address-book/{catdletId}", produces=MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Analytic(type = EventType.ADD)
    public ResponseEntity<?> createNewAddressBook(@PathVariable Long catdletId) throws JSONException{
        AddressBook addressBook = new AddressBook();
        JSONObject successObject = new JSONObject();
        Optional<User> optionalUser = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
        User user = null;
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        }

        Cardlet cardlet = cardletRepository.findOne(catdletId);

        JSONObject responce = new JSONObject();

        if (cardlet == null) {
            successObject.put("success", false);
            successObject.put("message", "Cardlet does not exist");

            return  new ResponseEntity<>(successObject.toString(), HttpStatus.BAD_REQUEST);
        }

        AddressBook existing = addressBookRepository.findOneByUserAndCardlet(user, cardlet);

        if (existing != null) {
            successObject.put("success", false);
            successObject.put("message", "This cardlet already exists in your address book");

            return  new ResponseEntity<>(successObject.toString(), HttpStatus.BAD_REQUEST);
        }

        addressBook.setUser(user);
        addressBook.setCardlet(cardlet);

        AddressBook result = addressBookRepository.save(addressBook);

        eventService.addContactEvent(cardlet);

        successObject.put("success", true);
        successObject.put("message", "Cardlet successfully added to your address book");
        return new ResponseEntity<>(successObject.toString(), HttpStatus.OK);
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
    public ResponseEntity<?> deleteAddressBook(@PathVariable Long id) {
        HttpHeaders textPlainHeaders = new HttpHeaders();
        textPlainHeaders.setContentType(MediaType.TEXT_PLAIN);
        Optional<User> optionalUser = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
        User user = null;
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        }

        Cardlet cardlet = cardletRepository.findOne(id);

        AddressBook addressBook = addressBookRepository.findOneByUserAndCardlet(user, cardlet);

        if (addressBook == null) {
            return new ResponseEntity<>("Error user validation", textPlainHeaders, HttpStatus.BAD_REQUEST);
        }

        log.debug("REST request to delete AddressBook : {}", id);
        addressBookRepository.delete(addressBook.getId());
        List<UserCardletDTO> usetCardletDTOs = addressBookService.userCardlets();
        return new ResponseEntity<>(usetCardletDTOs, HttpStatus.OK);
    }

}
