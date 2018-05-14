package itomy.sigterra.repository;

import itomy.sigterra.domain.CardletQuickBitesWidget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data JPA repository for the CardletQuickBitesWidget entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CardletQuickBitesWidgetRepository extends JpaRepository<CardletQuickBitesWidget, Long> {

    Page<CardletQuickBitesWidget> findAllByCardletId(Long cardletId, Pageable pageable);

    CardletQuickBitesWidget findByIdAndCardletId(Long id, Long cardletId);
}
