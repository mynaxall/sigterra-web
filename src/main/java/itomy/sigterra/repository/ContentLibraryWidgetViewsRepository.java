package itomy.sigterra.repository;

import itomy.sigterra.domain.Cardlet;
import itomy.sigterra.domain.CardletContentLibraryWidget;
import itomy.sigterra.domain.ContentLibraryWidgetViews;
import itomy.sigterra.domain.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


/**
 * Spring Data JPA repository for the ContentLibraryWidgetViews entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContentLibraryWidgetViewsRepository extends JpaRepository<ContentLibraryWidgetViews, Long> {

    @Query("select w from ContentLibraryWidgetViews w " +
        "where w.cardlet = :cardlet " +
        "and w.cardletContentLibraryWidget = :widget " +
        "and w.visitor = :visitor")
    ContentLibraryWidgetViews findByViews(@Param("cardlet") Cardlet cardlet,
                                          @Param("widget") CardletContentLibraryWidget widget,
                                          @Param("visitor") Visitor visitor);

    @Query("select count (v) from ContentLibraryWidgetViews v where v.cardletContentLibraryWidget.id = :widgetId")
    Long countViews(@Param("widgetId") Long widgetId);
}
