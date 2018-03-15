package itomy.sigterra.repository;

import itomy.sigterra.domain.CardletBackground;
import itomy.sigterra.domain.CardletHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Cardlet background entity.
 */
@Repository
@SuppressWarnings("unused")
public interface CardletBackgroundRepository extends JpaRepository<CardletBackground,Long>{
}
