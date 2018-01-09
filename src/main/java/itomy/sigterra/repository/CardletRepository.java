package itomy.sigterra.repository;

import itomy.sigterra.domain.Cardlet;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Cardlet entity.
 */
@SuppressWarnings("unused")
public interface CardletRepository extends JpaRepository<Cardlet,Long> {

    @Query("select cardlet from Cardlet cardlet where cardlet.user.login = ?#{principal.username}")
    List<Cardlet> findByUserIsCurrentUser();


    @Query("select cardlet from Cardlet cardlet where cardlet.id =:id and cardlet.user.login = ?#{principal.username}")
    Cardlet findOneByIdAndUserIsCurrentUser(@Param("id")Long id);

}
