package itomy.sigterra.service;

import itomy.sigterra.domain.Cardlet;
import itomy.sigterra.domain.Event;
import itomy.sigterra.domain.TopDomain;
import itomy.sigterra.domain.enumeration.EventType;
import itomy.sigterra.repository.CardletRepository;
import itomy.sigterra.service.dto.RecentDTO;
import itomy.sigterra.service.dto.TopDTO;
import itomy.sigterra.web.rest.vm.AnalyticStatVM;
import itomy.sigterra.web.rest.vm.AnalyticVM;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.*;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toList;

@Service
@Transactional
public class AnalyticService {

    @Inject
    private EventService eventService;
    @Inject
    private CardletRepository cardletRepository;

    public enum Period {DAY, WEEK, MONTH}

    private static final Map<Period, Supplier<Timestamp>> periods = new EnumMap<>(Period.class);

    static {
        periods.put(Period.DAY, () -> new Timestamp(DateTime.now().minusDays(1).toInstant().getMillis()));
        periods.put(Period.WEEK, () -> new Timestamp(DateTime.now().minusWeeks(1).toInstant().getMillis()));
        periods.put(Period.MONTH, () -> new Timestamp(DateTime.now().minusMonths(1).toInstant().getMillis()));
    }

    private static final List<EventType> TYPE_ALL_RECENT = Arrays.asList(EventType.ADD, EventType.READ, EventType.VIEW);
    private static final String DEFAULT_PERIOD = "day";
    private static final String DEFAULT_TYPE = "all";
    private static final Pageable DEFAULT_PAGEABLE = new PageRequest(0, 10);

    public List<Event> getStats(Long cardletId, String period) {
        if (cardletId == null) {
            return getStats(period);
        }
        Cardlet cardlet = cardletRepository.findOne(cardletId);
        Timestamp dateFrom = getDateFrom(period);

        return eventService.getAllEvents(cardlet, dateFrom);
    }

    public List<Event> getStats(String period) {
        Timestamp dateFrom = getDateFrom(period);

        return eventService.getAllEvents(dateFrom);
    }

    public Page<TopDomain> getTop(Long cardletId, String period, Pageable pageable) {
        if (cardletId == null) {
            return getTop(period, pageable);
        }
        Cardlet cardlet = cardletRepository.findOne(cardletId);
        Timestamp dateFrom = getDateFrom(period);

        return eventService.getAllClicksByCardlet(cardlet, dateFrom, pageable);
    }

    public Page<TopDomain> getTop(String period, Pageable pageable) {
        Timestamp dateFrom = getDateFrom(period);

        return eventService.getAllClicksForUserCardlets(dateFrom, pageable);
    }

    public Page<Event> getRecent(Long cardletId, String type, Pageable pageable) {
        if (cardletId == null) {
            return getRecent(type, pageable);
        }
        Cardlet cardlet = cardletRepository.findOne(cardletId);
        List<EventType> eventTypes = getTypes(type);

        return eventService.getAllRecentsByCardlet(cardlet, eventTypes, pageable);
    }

    public Page<Event> getRecent(String type, Pageable pageable) {
        List<EventType> eventTypes = getTypes(type);

        return eventService.getAllRecentForUser(eventTypes, pageable);
    }

    public AnalyticVM getAnalytic(String timezone) {
        AnalyticVM response = new AnalyticVM();
        response.setStatistics(new AnalyticStatVM(getStats(DEFAULT_PERIOD)));
        List<TopDomain> domains = getTop(DEFAULT_PERIOD, DEFAULT_PAGEABLE).getContent();
        response.setTop(domains.stream().map(TopDTO::new).collect(toList()));
        List<Event> recentEvents = getRecent(DEFAULT_TYPE, DEFAULT_PAGEABLE).getContent();
        response.setRecent(recentEvents.stream().map(event -> new RecentDTO(event, timezone)).collect(toList()));

        return response;
    }

    private Timestamp getDateFrom(String period) {
        Period enumPeriod;
        try {
            enumPeriod = Period.valueOf(period.toUpperCase());
        } catch (IllegalArgumentException ex) {
            enumPeriod = Period.DAY;
        }
        return periods.get(enumPeriod).get();
    }

    private List<EventType> getTypes(String type) {
        if ("all".equalsIgnoreCase(type) || "click".equalsIgnoreCase(type)) {
            return TYPE_ALL_RECENT;
        }
        try {
            EventType eventType = EventType.valueOf(type.toUpperCase());
            return Collections.singletonList(eventType);
        } catch (IllegalArgumentException ex) {
            return TYPE_ALL_RECENT;
        }
    }
}
