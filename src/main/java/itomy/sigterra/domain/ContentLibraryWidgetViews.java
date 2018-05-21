package itomy.sigterra.domain;


import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * A ContentLibraryWidgetViews.
 */
@Entity
@Table(name = "content_library_widget_views")
public class ContentLibraryWidgetViews implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Timestamp createdDate;

    @ManyToOne
    @NotNull
    private Visitor visitor;

    @ManyToOne
    @NotNull
    private Cardlet cardlet;

    @ManyToOne
    private CardletContentLibraryWidget cardletContentLibraryWidget;

    public ContentLibraryWidgetViews() {
    }

    public ContentLibraryWidgetViews(Timestamp createdDate, Visitor visitor, Cardlet cardlet, CardletContentLibraryWidget cardletContentLibraryWidget) {
        this.createdDate = createdDate;
        this.visitor = visitor;
        this.cardlet = cardlet;
        this.cardletContentLibraryWidget = cardletContentLibraryWidget;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public Visitor getVisitor() {
        return visitor;
    }

    public void setVisitor(Visitor visitor) {
        this.visitor = visitor;
    }

    public Cardlet getCardlet() {
        return cardlet;
    }

    public void setCardlet(Cardlet cardlet) {
        this.cardlet = cardlet;
    }

    public CardletContentLibraryWidget getCardletContentLibraryWidget() {
        return cardletContentLibraryWidget;
    }

    public void setCardletContentLibraryWidget(CardletContentLibraryWidget cardletContentLibraryWidget) {
        this.cardletContentLibraryWidget = cardletContentLibraryWidget;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ContentLibraryWidgetViews contentLibraryWidgetViews = (ContentLibraryWidgetViews) o;
        if (contentLibraryWidgetViews.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), contentLibraryWidgetViews.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ContentLibraryWidgetViews{" +
            "id=" + getId() +
            "}";
    }
}
