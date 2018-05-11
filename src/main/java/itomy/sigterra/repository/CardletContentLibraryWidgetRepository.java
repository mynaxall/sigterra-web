package itomy.sigterra.repository;

import itomy.sigterra.domain.CardletContentLibraryWidget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


/**
 * Spring Data JPA repository for the CardletContentLibraryWidget entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CardletContentLibraryWidgetRepository extends JpaRepository<CardletContentLibraryWidget, Long> {

    Page<CardletContentLibraryWidget> findAllByCardletId(Long cardletId, Pageable pageable);

    CardletContentLibraryWidget findByIdAndCardletId(Long id, Long cardletId);

    @Modifying
    @Transactional
    @Query(value = "delete from cardlet_content_library_widget where id = :id", nativeQuery = true)
    void deleteById(@Param("id") Long id);

}
