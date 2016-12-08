package itomy.sigterra.repository;

import itomy.sigterra.domain.Business;
import itomy.sigterra.domain.Cardlet;

import itomy.sigterra.domain.User;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Cardlet entity.
 */
@SuppressWarnings("unused")
public interface CardletRepository extends JpaRepository<Cardlet,Long> {

    @Query("select cardlet from Cardlet cardlet where cardlet.user.login = ?#{principal.username}")
    List<Cardlet> findByUserIsCurrentUser();

    List<Cardlet> findAllByUser(User user);


}
