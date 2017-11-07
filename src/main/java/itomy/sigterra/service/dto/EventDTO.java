package itomy.sigterra.service.dto;

import itomy.sigterra.domain.enumeration.EventType;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the Event entity.
 */
public class EventDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate createdDate;

    @NotNull
    private EventType type;

    private String description;


    private Long visitorId;

    private Long cardletId;

    private Long itemId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getVisitorId() {
        return visitorId;
    }

    public void setVisitorId(Long visitorId) {
        this.visitorId = visitorId;
    }

    public Long getCardletId() {
        return cardletId;
    }

    public void setCardletId(Long cardletId) {
        this.cardletId = cardletId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EventDTO eventDTO = (EventDTO) o;

        return Objects.equals(id, eventDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "EventDTO{" +
            "id=" + id +
            ", createdDate='" + createdDate + "'" +
            ", type='" + type + "'" +
            ", description='" + description + "'" +
            '}';
    }
}
