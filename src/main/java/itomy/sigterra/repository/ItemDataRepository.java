package itomy.sigterra.repository;

import itomy.sigterra.domain.ItemData;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ItemData entity.
 */
@SuppressWarnings("unused")
public interface ItemDataRepository extends JpaRepository<ItemData,Long> {

}
