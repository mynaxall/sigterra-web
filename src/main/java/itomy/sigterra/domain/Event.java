package itomy.sigterra.domain;


import itomy.sigterra.domain.enumeration.EventType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * A Event.
 */
@Entity
@Table(name = "event")
public class Event implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Timestamp createdDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private EventType type;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @NotNull
    private Visitor visitor;

    @ManyToOne
    @NotNull
    private Cardlet cardlet;

    @ManyToOne
    private Item item;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public Event createdDate(Timestamp createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public EventType getType() {
        return type;
    }

    public Event type(EventType type) {
        this.type = type;
        return this;
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

    public Visitor getVisitor() {
        return visitor;
    }

    public Event visitor(Visitor visitor) {
        this.visitor = visitor;
        return this;
    }

    public void setVisitor(Visitor visitor) {
        this.visitor = visitor;
    }

    public Cardlet getCardlet() {
        return cardlet;
    }

    public Event cardlet(Cardlet cardlet) {
        this.cardlet = cardlet;
        return this;
    }

    public void setCardlet(Cardlet cardlet) {
        this.cardlet = cardlet;
    }

    public Item getItem() {
        return item;
    }

    public Event item(Item item) {
        this.item = item;
        return this;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Event event = (Event) o;
        if (event.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, event.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Event{" +
            "id=" + id +
            ", createdDate='" + createdDate + "'" +
            ", type='" + type + "'" +
            ", visitor = '" + visitor + "'" +
            ", description='" + description + "'" +
            '}';
    }
}
