package itomy.sigterra.web.rest.vm;

import itomy.sigterra.service.dto.RecentDTO;
import itomy.sigterra.service.dto.TopDTO;

import java.util.List;

/**
 * View Model object for analytic statistic response
 */
public class AnalyticVM {

    private AnalyticStatVM statistics;
    private List<TopDTO> top;
    private List<RecentDTO> recent;

    public AnalyticStatVM getStatistics() {
        return statistics;
    }

    public void setStatistics(AnalyticStatVM statistics) {
        this.statistics = statistics;
    }

    public List<TopDTO> getTop() {
        return top;
    }

    public void setTop(List<TopDTO> top) {
        this.top = top;
    }

    public List<RecentDTO> getRecent() {
        return recent;
    }

    public void setRecent(List<RecentDTO> recent) {
        this.recent = recent;
    }
}
