package itomy.sigterra.repository;

import itomy.sigterra.domain.CardletQuickBitesWidget;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the CardletQuickBitesWidget entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CardletQuickBitesWidgetRepository extends JpaRepository<CardletQuickBitesWidget, Long> {

}
