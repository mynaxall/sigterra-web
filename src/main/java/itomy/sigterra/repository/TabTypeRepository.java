package itomy.sigterra.repository;

import itomy.sigterra.domain.TabType;

import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the TabType entity.
 */
@SuppressWarnings("unused")
public interface TabTypeRepository extends JpaRepository<TabType,Long> {

    List<TabType> findByType(String type);


}
