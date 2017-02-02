package itomy.sigterra.repository;

import itomy.sigterra.domain.InputProperties;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the InputProperties entity.
 */
@SuppressWarnings("unused")
public interface InputPropertiesRepository extends JpaRepository<InputProperties,Long> {

}
