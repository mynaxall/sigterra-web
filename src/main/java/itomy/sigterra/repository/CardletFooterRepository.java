package itomy.sigterra.repository;

import itomy.sigterra.domain.CardletBackground;
import itomy.sigterra.domain.CardletFooter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Cardlet footer entity.
 */
@Repository
@SuppressWarnings("unused")
public interface CardletFooterRepository extends JpaRepository<CardletFooter,Long>{
}
