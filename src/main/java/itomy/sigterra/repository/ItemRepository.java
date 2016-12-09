package itomy.sigterra.repository;

import itomy.sigterra.domain.Business;
import itomy.sigterra.domain.Cardlet;
import itomy.sigterra.domain.Item;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Item entity.
 */
@SuppressWarnings("unused")
public interface ItemRepository extends JpaRepository<Item,Long> {

    List<Item> findAllByCardlet(Cardlet cardlet);

}
