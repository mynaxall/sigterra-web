package itomy.sigterra.repository;

import itomy.sigterra.domain.Cardlet;
import itomy.sigterra.domain.Event;
import itomy.sigterra.domain.TopDomain;
import itomy.sigterra.domain.User;
import itomy.sigterra.domain.enumeration.EventType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

/**
 * Spring Data JPA repository for the Event entity.
 */
@SuppressWarnings("unused")
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("select e from Event e where e.item.id = :id and (e.type = 'PDF_READ' or e.type = 'PDF_CLICK')")
    List<Event> findByItemId(@Param("id") Long id);

    @Query("select e from Event e where e.cardlet = :cardlet and e.createdDate > :dateFrom")
    List<Event> findAllEvents(@Param("cardlet") Cardlet cardlet,
                              @Param("dateFrom") Timestamp dateFrom);

    @Query("select e from Event e where e.createdDate > :dateFrom and e.cardlet.user = :user")
    List<Event> findAllEvents(@Param("user") User user,
                              @Param("dateFrom") Timestamp dateFrom);

    @Query("select new itomy.sigterra.domain.TopDomain(e.visitor, count(e)) from Event e " +
        "where e.type = 'CLICK' and e.cardlet = :cardlet and e.createdDate > :dateFrom " +
        "group by e.visitor order by count(e) DESC")
    Page<TopDomain> findAllClicksByCardlet(@Param("cardlet") Cardlet cardlet,
                                           @Param("dateFrom") Timestamp dateFrom,
                                           Pageable pageable);

    @Query("select new itomy.sigterra.domain.TopDomain(e.visitor, count(e)) from Event e " +
        "where e.type = 'CLICK' and e.cardlet.user = :user and e.createdDate > :dateFrom " +
        "group by e.visitor order by count(e) DESC")
    Page<TopDomain> findAllClicksForUserCardlets(@Param("user") User user,
                                                 @Param("dateFrom") Timestamp dateFrom,
                                                 Pageable pageable);


    @Query("select e from Event e " +
        "where e.type in :types and e.cardlet = :cardlet " +
        "order by e.createdDate DESC")
    Page<Event> findAllRecentsByCardlet(@Param("cardlet") Cardlet cardlet,
                                        @Param("types") List<EventType> types,
                                        Pageable pageable);

    @Query("select e from Event e " +
        "where e.type in :types and e.cardlet.user = :user " +
        "order by e.createdDate DESC")
    Page<Event> findAllRecentForUser(@Param("user") User user,
                                     @Param("types") List<EventType> types,
                                     Pageable pageable);
}
