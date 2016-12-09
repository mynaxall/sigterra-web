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

    @Column(name = "user_name")
    private String userName;

    @Column(name = "job_position")
    private String jobPosition;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "company_site")
    private String companySite;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

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

    public String getUserName() {
        return userName;
    }

    public Business userName(String userName) {
        this.userName = userName;
        return this;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getJobPosition() {
        return jobPosition;
    }

    public Business jobPosition(String jobPosition) {
        this.jobPosition = jobPosition;
        return this;
    }

    public void setJobPosition(String jobPosition) {
        this.jobPosition = jobPosition;
    }

    public String getCompanyName() {
        return companyName;
    }

    public Business companyName(String companyName) {
        this.companyName = companyName;
        return this;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanySite() {
        return companySite;
    }

    public Business companySite(String companySite) {
        this.companySite = companySite;
        return this;
    }

    public void setCompanySite(String companySite) {
        this.companySite = companySite;
    }

    public String getEmail() {
        return email;
    }

    public Business email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public Business phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public Business address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
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
            ", userName='" + userName + "'" +
            ", jobPosition='" + jobPosition + "'" +
            ", companyName='" + companyName + "'" +
            ", companySite='" + companySite + "'" +
            ", email='" + email + "'" +
            ", phone='" + phone + "'" +
            ", address='" + address + "'" +
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
