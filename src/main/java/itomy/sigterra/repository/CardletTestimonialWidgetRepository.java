package itomy.sigterra.repository;

import itomy.sigterra.domain.CardletTestimonialWidget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.transaction.annotation.Transactional;


/**
 * Spring Data JPA repository for the CardletTestimonialWidget entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CardletTestimonialWidgetRepository extends JpaRepository<CardletTestimonialWidget, Long> {

    Page<CardletTestimonialWidget> findAllByCardletId(Long cardletId, Pageable pageable);

    CardletTestimonialWidget findByIdAndCardletId(Long id, Long cardletId);

    @Modifying
    @Transactional
    @Query(value = "delete from cardlet_testimonial_widget where id = :id", nativeQuery = true)
    void deleteById(@Param("id") Long id);
}
