package itomy.sigterra.repository;

import itomy.sigterra.domain.AddressBook;

import itomy.sigterra.domain.Cardlet;
import itomy.sigterra.domain.User;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the AddressBook entity.
 */
@SuppressWarnings("unused")
public interface AddressBookRepository extends JpaRepository<AddressBook,Long> {

    @Query("select addressBook from AddressBook addressBook where addressBook.user.login = ?#{principal.username}")
    List<AddressBook> findByUserIsCurrentUser();


    AddressBook findOneByUserAndCardlet(User user, Cardlet cardlet);

    AddressBook findOneByIdAndUser(Long id, User user);

}
