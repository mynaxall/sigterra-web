package itomy.sigterra.repository;

import itomy.sigterra.domain.Business;

import itomy.sigterra.domain.Cardlet;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Business entity.
 */
@SuppressWarnings("unused")
public interface BusinessRepository extends JpaRepository<Business,Long> {

    List<Business> findAllByCardlet(Cardlet cardlet);

}
