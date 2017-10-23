package itomy.sigterra.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import itomy.sigterra.domain.enumeration.EventType;

/**
 * A DTO for the Event entity.
 */
public class EventDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate createdDate;

    @NotNull
    private EventType type;


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

        if ( ! Objects.equals(id, eventDTO.id)) return false;

        return true;
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
            '}';
    }
}
