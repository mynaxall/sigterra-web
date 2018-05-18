package itomy.sigterra.repository;

import itomy.sigterra.domain.Cardlet;
import itomy.sigterra.domain.CardletContentLibraryWidget;
import itomy.sigterra.domain.ContentLibraryWidgetLikes;
import itomy.sigterra.domain.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


/**
 * Spring Data JPA repository for the ContentLibraryWidgetLikes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContentLibraryWidgetLikesRepository extends JpaRepository<ContentLibraryWidgetLikes, Long> {

    @Query("select l from ContentLibraryWidgetLikes l " +
        "where l.cardlet = :cardlet " +
        "and l.cardletContentLibraryWidget = :widget " +
        "and l.visitor = :visitor")
    ContentLibraryWidgetLikes findByLike(@Param("cardlet") Cardlet cardlet,
                                         @Param("widget") CardletContentLibraryWidget widget,
                                         @Param("visitor") Visitor visitor);

    @Query("select count (l) from ContentLibraryWidgetLikes l where l.cardletContentLibraryWidget.id = :widgetId")
    Long countLikes(@Param("widgetId") Long widgetId);

}
