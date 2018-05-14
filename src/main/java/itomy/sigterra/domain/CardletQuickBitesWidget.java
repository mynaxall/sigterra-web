package itomy.sigterra.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 * A CardletQuickBitesWidget.
 */
@Entity
@Table(name = "cardlet_quick_bites_widget")
public class CardletQuickBitesWidget implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "title", length = 30, nullable = false)
    private String title;

    @Size(max = 200)
    @Column(name = "description", length = 200)
    private String description;

    @JsonIgnore
    @ManyToOne
    private Cardlet cardlet;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public CardletQuickBitesWidget title(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Cardlet getCardlet() {
        return cardlet;
    }

    public CardletQuickBitesWidget cardlet(Cardlet cardlet) {
        this.cardlet = cardlet;
        return this;
    }

    public void setCardlet(Cardlet cardlet) {
        this.cardlet = cardlet;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CardletQuickBitesWidget cardletQuickBitesWidget = (CardletQuickBitesWidget) o;
        if (cardletQuickBitesWidget.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cardletQuickBitesWidget.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CardletQuickBitesWidget{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            "}";
    }
}
