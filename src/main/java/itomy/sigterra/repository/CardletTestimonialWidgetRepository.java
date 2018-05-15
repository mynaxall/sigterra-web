package itomy.sigterra.repository;

import itomy.sigterra.domain.CardletTestimonialWidget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Spring Data JPA repository for the CardletTestimonialWidget entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CardletTestimonialWidgetRepository extends JpaRepository<CardletTestimonialWidget, Long> {

    List<CardletTestimonialWidget> findAllByCardletId(Long cardletId);

    CardletTestimonialWidget findByIdAndCardletId(Long id, Long cardletId);

    @Modifying
    @Transactional
    @Query(value = "delete from cardlet_testimonial_widget where id = :id", nativeQuery = true)
    void deleteById(@Param("id") Long id);
}
