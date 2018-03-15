package itomy.sigterra.domain;

import itomy.sigterra.domain.enumeration.CardletFooterIndex;

import javax.persistence.*;
import java.util.Objects;

/*
   data for "Edit page", tab: footer
 */


@Entity
@Table(name = "cardlet_footer")
public class CardletFooter extends AbstractAuditingEntity{


    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "position")
    @Enumerated(EnumType.ORDINAL)
    private CardletFooterIndex position;

    @Column(name = "name")
    private String name;

    @Column(name = "url")
    private String url;

    @Column(name = "logo")
    private String logo;

    @Column(name="title")
    private String title;

    @ManyToOne
    @JoinColumn(name = "cardlet_id")
    private Cardlet cardlet;

    public CardletFooter() {
        //for JPA
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardletFooter that = (CardletFooter) o;
        if(that.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CardletFooter{" +
            "id=" + id +
            ", index=" + position +
            ", name='" + name + '\'' +
            ", url='" + url + '\'' +
            ", logo='" + logo + '\'' +
            ", cardlet=" + cardlet +
            '}';
    }

    public CardletFooterIndex getPosition() {
        return position;
    }

    public void setPosition(CardletFooterIndex position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Long getId() {
        return id;
    }

    public Cardlet getCardlet() {
        return cardlet;
    }

    public void setCardlet(Cardlet cardlet) {
        this.cardlet = cardlet;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
