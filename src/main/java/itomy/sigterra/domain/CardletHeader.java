package itomy.sigterra.domain;


import javax.persistence.*;
import java.util.Objects;

/*
   data for "Edit page", tab: header
 */
@Entity
@Table(name="cardlet_header")
public class CardletHeader extends AbstractAuditingEntity implements EntityWithLongId {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "cta_button_color")
    private String ctaButtonColor;

    @Column(name = "cta_text")
    private String ctaText;

    @Column(name = "logo")
    private String logo;

    @Column(name = "photo")
    private String photo;

    @Column(name = "name")
    private String name;

    @Column(name = "title")
    private String title;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @OneToOne
    @JoinColumn(name = "cardlet_id")
    private Cardlet cardlet;

    public CardletHeader(String ctaButtonColor, String ctaText, String logo, String photo, String name, String title, String phone, String email, Cardlet cardlet) {
        this.ctaButtonColor = ctaButtonColor;
        this.ctaText = ctaText;
        this.logo = logo;
        this.photo = photo;
        this.name = name;
        this.title = title;
        this.phone = phone;
        this.email = email;
        this.cardlet = cardlet;
    }

    public CardletHeader() {
        //for Jackson
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardletHeader that = (CardletHeader) o;
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
        return "CardletHeader{" +
            "id=" + id +
            ", ctaButtonColor='" + ctaButtonColor + '\'' +
            ", ctaText='" + ctaText + '\'' +
            ", logo='" + logo + '\'' +
            ", photo='" + photo + '\'' +
            ", name='" + name + '\'' +
            ", title='" + title + '\'' +
            ", phone='" + phone + '\'' +
            ", email='" + email + '\'' +
            ", cardlet=" + cardlet +
            '}';
    }

    public String getCtaButtonColor() {
        return ctaButtonColor;
    }

    public void setCtaButtonColor(String ctaButtonColor) {
        this.ctaButtonColor = ctaButtonColor;
    }

    public String getCtaText() {
        return ctaText;
    }

    public void setCtaText(String ctaText) {
        this.ctaText = ctaText;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

}
