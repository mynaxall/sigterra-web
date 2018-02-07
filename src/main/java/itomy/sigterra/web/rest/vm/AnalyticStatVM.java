package itomy.sigterra.web.rest.vm;

import itomy.sigterra.domain.Event;
import itomy.sigterra.domain.enumeration.EventType;

import java.util.List;

import static itomy.sigterra.domain.enumeration.EventType.*;

/**
 * View Model object for analytic statistic response
 */
public class AnalyticStatVM {

    private Long uniqueViews;
    private Long adds;
    private Long clicks;
    private Long reads;

    public AnalyticStatVM(List<Event> eventList) {
        this.uniqueViews = countByTypeUnique(eventList, VIEW);
        this.adds        = countByType(eventList, ADD);
        this.clicks      = countByType(eventList, CLICK);
        this.reads       = countByType(eventList, READ);
    }

    private long countByType(List<Event> events, EventType type) {
        return events.stream()
            .filter(e -> e.getType() == type)
            .count();
    }

    private long countByTypeUnique(List<Event> events, EventType type) {
        return events.stream()
            .filter(e -> e.getType() == type)
            .map(e -> e.getVisitor().getId())
            .distinct()
            .count();
    }

    public Long getUniqueViews() {
        return uniqueViews;
    }

    public void setUniqueViews(Long uniqueViews) {
        this.uniqueViews = uniqueViews;
    }

    public Long getAdds() {
        return adds;
    }

    public void setAdds(Long adds) {
        this.adds = adds;
    }

    public Long getClicks() {
        return clicks;
    }

    public void setClicks(Long clicks) {
        this.clicks = clicks;
    }

    public Long getReads() {
        return reads;
    }

    public void setReads(Long reads) {
        this.reads = reads;
    }
}
