package itomy.sigterra.service.util;

import itomy.sigterra.domain.AddressBook;
import itomy.sigterra.domain.Cardlet;
import itomy.sigterra.repository.AddressBookRepository;
import itomy.sigterra.service.CardletService;
import itomy.sigterra.service.dto.UserCardletDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexander on 6/26/17.
 */

@Service
@Transactional
public class AddressBookService {

    private final Logger log = LoggerFactory.getLogger(AddressBookService.class);

    @Inject
    private CardletService cardletService;

    @Inject
    private AddressBookRepository addressBookRepository;

    public List<UserCardletDTO> userCardlets(){

        List<AddressBook> addressBookList = addressBookRepository.findByUserIsCurrentUser();

        List<UserCardletDTO> usetCardletDTOs = new ArrayList<>();
        for (AddressBook addressBook : addressBookList) {
            UserCardletDTO userCardletDTO = cardletService.createUserCatdletDTO(addressBook.getCardlet());
            usetCardletDTOs.add(userCardletDTO);
        }

        return usetCardletDTOs;
    }
}
