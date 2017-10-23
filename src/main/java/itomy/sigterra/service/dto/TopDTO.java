package itomy.sigterra.service.dto;

import itomy.sigterra.domain.TopDomain;
import itomy.sigterra.domain.Visitor;

public class TopDTO {

    private String name;
    private String location;
    private long clicks;

    public TopDTO(TopDomain topDomain) {
        Visitor visitor = topDomain.getVisitor();
        this.name = visitor.getName();
        this.location = visitor.getCountry() == null || visitor.getCity() == null
            ? null
            : visitor.getCountry() + " " + visitor.getCity();
        this.clicks = topDomain.getClicks();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public long getClicks() {
        return clicks;
    }

    public void setClicks(long clicks) {
        this.clicks = clicks;
    }
}
