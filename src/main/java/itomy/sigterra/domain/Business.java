package itomy.sigterra.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Business.
 */
@Entity
@Table(name = "business")
public class Business implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "modified_date")
    private LocalDate modifiedDate;

    @Column(name = "pisition")
    private Integer pisition;

    @Column(name = "icon")
    private String icon;

    @Column(name = "twitter")
    private String twitter;

    @Column(name = "facebook")
    private String facebook;

    @Column(name = "google")
    private String google;

    @Column(name = "linked_in")
    private String linkedIn;

    @Column(name = "photo")
    private String photo;

    @Column(name = "main_color")
    private String mainColor;

    @Column(name = "color")
    private String color;

    @ManyToOne
    private Cardlet cardlet;

    @ManyToOne
    private TabType tabType;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(unique = true)
    private InputProperties userName;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(unique = true)
    private InputProperties userEmail;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(unique = true)
    private InputProperties phone;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(unique = true)
    private InputProperties address;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(unique = true)
    private InputProperties company;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(unique = true)
    private InputProperties site;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(unique = true)
    private InputProperties job;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Business name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public Business createdDate(LocalDate createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDate getModifiedDate() {
        return modifiedDate;
    }

    public Business modifiedDate(LocalDate modifiedDate) {
        this.modifiedDate = modifiedDate;
        return this;
    }

    public void setModifiedDate(LocalDate modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Integer getPisition() {
        return pisition;
    }

    public Business pisition(Integer pisition) {
        this.pisition = pisition;
        return this;
    }

    public void setPisition(Integer pisition) {
        this.pisition = pisition;
    }

    public String getIcon() {
        return icon;
    }

    public Business icon(String icon) {
        this.icon = icon;
        return this;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTwitter() {
        return twitter;
    }

    public Business twitter(String twitter) {
        this.twitter = twitter;
        return this;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getFacebook() {
        return facebook;
    }

    public Business facebook(String facebook) {
        this.facebook = facebook;
        return this;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getGoogle() {
        return google;
    }

    public Business google(String google) {
        this.google = google;
        return this;
    }

    public void setGoogle(String google) {
        this.google = google;
    }

    public String getLinkedIn() {
        return linkedIn;
    }

    public Business linkedIn(String linkedIn) {
        this.linkedIn = linkedIn;
        return this;
    }

    public void setLinkedIn(String linkedIn) {
        this.linkedIn = linkedIn;
    }

    public String getPhoto() {
        return photo;
    }

    public Business photo(String photo) {
        this.photo = photo;
        return this;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getMainColor() {
        return mainColor;
    }

    public Business mainColor(String mainColor) {
        this.mainColor = mainColor;
        return this;
    }

    public void setMainColor(String mainColor) {
        this.mainColor = mainColor;
    }

    public String getColor() {
        return color;
    }

    public Business color(String color) {
        this.color = color;
        return this;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Cardlet getCardlet() {
        return cardlet;
    }

    public Business cardlet(Cardlet cardlet) {
        this.cardlet = cardlet;
        return this;
    }

    public void setCardlet(Cardlet cardlet) {
        this.cardlet = cardlet;
    }

    public TabType getTabType() {
        return tabType;
    }

    public Business tabType(TabType tabType) {
        this.tabType = tabType;
        return this;
    }

    public void setTabType(TabType tabType) {
        this.tabType = tabType;
    }

    public InputProperties getUserName() {
        return userName;
    }

    public Business userName(InputProperties inputProperties) {
        this.userName = inputProperties;
        return this;
    }

    public void setUserName(InputProperties inputProperties) {
        this.userName = inputProperties;
    }

    public InputProperties getUserEmail() {
        return userEmail;
    }

    public Business userEmail(InputProperties inputProperties) {
        this.userEmail = inputProperties;
        return this;
    }

    public void setUserEmail(InputProperties inputProperties) {
        this.userEmail = inputProperties;
    }

    public InputProperties getPhone() {
        return phone;
    }

    public Business phone(InputProperties inputProperties) {
        this.phone = inputProperties;
        return this;
    }

    public void setPhone(InputProperties inputProperties) {
        this.phone = inputProperties;
    }

    public InputProperties getAddress() {
        return address;
    }

    public Business address(InputProperties inputProperties) {
        this.address = inputProperties;
        return this;
    }

    public void setAddress(InputProperties inputProperties) {
        this.address = inputProperties;
    }

    public InputProperties getCompany() {
        return company;
    }

    public Business company(InputProperties inputProperties) {
        this.company = inputProperties;
        return this;
    }

    public void setCompany(InputProperties inputProperties) {
        this.company = inputProperties;
    }

    public InputProperties getSite() {
        return site;
    }

    public Business site(InputProperties inputProperties) {
        this.site = inputProperties;
        return this;
    }

    public void setSite(InputProperties inputProperties) {
        this.site = inputProperties;
    }

    public InputProperties getJob() {
        return job;
    }

    public Business job(InputProperties inputProperties) {
        this.job = inputProperties;
        return this;
    }

    public void setJob(InputProperties inputProperties) {
        this.job = inputProperties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Business business = (Business) o;
        if(business.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, business.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Business{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", createdDate='" + createdDate + "'" +
            ", modifiedDate='" + modifiedDate + "'" +
            ", pisition='" + pisition + "'" +
            ", icon='" + icon + "'" +
            ", twitter='" + twitter + "'" +
            ", facebook='" + facebook + "'" +
            ", google='" + google + "'" +
            ", linkedIn='" + linkedIn + "'" +
            ", photo='" + photo + "'" +
            ", mainColor='" + mainColor + "'" +
            ", color='" + color + "'" +
            '}';
    }
}
