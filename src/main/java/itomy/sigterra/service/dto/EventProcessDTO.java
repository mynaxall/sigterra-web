package itomy.sigterra.service.dto;

import itomy.sigterra.domain.Event;
import itomy.sigterra.domain.User;

public class EventProcessDTO {
    private Event event;
    private User user;
    private String ip;
    private String agent;

    public EventProcessDTO() {
    }

    public EventProcessDTO(Event event, User user, String ip, String agent) {
        this.event = event;
        this.user = user;
        this.ip = ip;
        this.agent = agent;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    @Override
    public String toString() {
        return "EventProcessDTO{" +
            "event=" + event +
            ", user=" + user +
            ", ip='" + ip + '\'' +
            ", agent='" + agent + '\'' +
            '}';
    }
}
