package itomy.sigterra.domain;

import javax.persistence.*;
import java.util.Objects;

/*
   data for "Edit page", tab: links
 */


@Entity
@Table(name = "cardlet_link")
public class CardletLinks extends AbstractAuditingEntity implements EntityWithLongId {


    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name1")
    private String name1;
    @Column(name = "url1")
    private String url1;
    @Column(name = "logo1")
    private String logo1;

    @Column(name = "name2")
    private String name2;
    @Column(name = "url2")
    private String url2;
    @Column(name = "logo2")
    private String logo2;

    @Column(name = "name3")
    private String name3;
    @Column(name = "url3")
    private String url3;
    @Column(name = "logo3")
    private String logo3;

    @Column(name = "title")
    private String title;

    @OneToOne
    @JoinColumn(name = "cardlet_id")
    private Cardlet cardlet;

    public CardletLinks() {
        //for JPA
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardletLinks that = (CardletLinks) o;
        if (that.id == null || id == null) {
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
        return "CardletLinks{" +
            "id=" + id +
            ", name1='" + name1 + '\'' +
            ", url1='" + url1 + '\'' +
            ", logo1='" + logo1 + '\'' +
            ", name2='" + name2 + '\'' +
            ", url2='" + url2 + '\'' +
            ", logo2='" + logo2 + '\'' +
            ", name3='" + name3 + '\'' +
            ", url3='" + url3 + '\'' +
            ", logo3='" + logo3 + '\'' +
            ", title='" + title + '\'' +
            ", cardlet=" + cardlet +
            '}';
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getUrl1() {
        return url1;
    }

    public void setUrl1(String url1) {
        this.url1 = url1;
    }

    public String getLogo1() {
        return logo1;
    }

    public void setLogo1(String logo1) {
        this.logo1 = logo1;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public String getUrl2() {
        return url2;
    }

    public void setUrl2(String url2) {
        this.url2 = url2;
    }

    public String getLogo2() {
        return logo2;
    }

    public void setLogo2(String logo2) {
        this.logo2 = logo2;
    }

    public String getName3() {
        return name3;
    }

    public void setName3(String name3) {
        this.name3 = name3;
    }

    public String getUrl3() {
        return url3;
    }

    public void setUrl3(String url3) {
        this.url3 = url3;
    }

    public String getLogo3() {
        return logo3;
    }

    public void setLogo3(String logo3) {
        this.logo3 = logo3;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Cardlet getCardlet() {
        return cardlet;
    }

    public void setCardlet(Cardlet cardlet) {
        this.cardlet = cardlet;
    }

    public Long getId() {
        return id;
    }

}
