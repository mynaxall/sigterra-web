package itomy.sigterra.repository;

import itomy.sigterra.domain.CardletQuickBitesWidget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data JPA repository for the CardletQuickBitesWidget entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CardletQuickBitesWidgetRepository extends JpaRepository<CardletQuickBitesWidget, Long> {

    List<CardletQuickBitesWidget> findAllByCardletId(Long cardletId);

    CardletQuickBitesWidget findByIdAndCardletId(Long id, Long cardletId);
}
