package itomy.sigterra.service.dto;

import itomy.sigterra.domain.Cardlet;
import itomy.sigterra.domain.Event;
import itomy.sigterra.domain.Visitor;
import itomy.sigterra.service.util.DateFormatUtil;

public class RecentDTO {
    private String type;
    private String user;
    private String time;
    private String name;
    private String description;

    public RecentDTO() {
    }

    public RecentDTO(Event event) {
        Visitor visitor = event.getVisitor();
        Cardlet cardlet = event.getCardlet();

        this.user = visitor.getName();
        this.type = event.getType().toString().toLowerCase();
        this.time = DateFormatUtil.format(event.getCreatedDate());
        this.name = event.getItem() == null
            ? cardlet.getName()
            : event.getItem().getName();
        this.description = event.getDescription();
    }

    public RecentDTO(Event event, String timeZone) {
        Visitor visitor = event.getVisitor();
        Cardlet cardlet = event.getCardlet();

        this.user = visitor.getName();
        this.type = event.getType().toString().toLowerCase();
        this.time = DateFormatUtil.format(event.getCreatedDate(), timeZone);
        this.name = event.getItem() == null
            ? cardlet.getName()
            : event.getItem().getName();
        this.description = event.getDescription();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
