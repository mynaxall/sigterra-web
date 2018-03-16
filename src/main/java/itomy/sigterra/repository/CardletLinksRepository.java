package itomy.sigterra.repository;

import itomy.sigterra.domain.CardletLinks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Cardlet links entity.
 */
@Repository
@SuppressWarnings("unused")
public interface CardletLinksRepository extends JpaRepository<CardletLinks, Long> {

}
