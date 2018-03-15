package itomy.sigterra.domain;

import javax.persistence.*;

/*
   data for "Edit page", tab: footer
 */


@Entity
@Table(name = "cardlet_footer")
public class CardletFooter extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "facebook_link")
    private String facebookLink;

    @Column(name = "twitter_link")
    private String twitterLink;

    @Column(name = "linkedin_link")
    private String linkedin_link;

    @OneToOne
    @JoinColumn(name = "cardlet_id")
    private Cardlet cardlet;

    public CardletFooter() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFacebookLink() {
        return this.facebookLink;
    }

    public void setFacebookLink(String facebookLink) {
        this.facebookLink = facebookLink;
    }

    public String getTwitterLink() {
        return this.twitterLink;
    }

    public void setTwitterLink(String twitterLink) {
        this.twitterLink = twitterLink;
    }

    public String getLinkedin_link() {
        return this.linkedin_link;
    }

    public void setLinkedin_link(String linkedin_link) {
        this.linkedin_link = linkedin_link;
    }

    public Cardlet getCardlet() {
        return this.cardlet;
    }

    public void setCardlet(Cardlet cardlet) {
        this.cardlet = cardlet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CardletFooter that = (CardletFooter) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "CardletFooter{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", facebookLink='" + facebookLink + '\'' +
            ", twitterLink='" + twitterLink + '\'' +
            ", linkedin_link='" + linkedin_link + '\'' +
            ", cardlet=" + cardlet +
            '}';
    }
}
