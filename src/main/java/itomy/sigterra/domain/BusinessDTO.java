package itomy.sigterra.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A Business.
 */

public class BusinessDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    private Long id;

    private String name;

    private LocalDate createdDate;

    private LocalDate modifiedDate;

    private Integer pisition;

    private String icon;

    private String userName;

    private String jobPosition;

    private String companyName;

    private String companySite;

    private String email;

    private String phone;

    private String address;

    private String twitter;

    private String facebook;

    private String google;

    private String linkedIn;

    private String photo;

    private String mainColor;

    private String color;

    @ManyToOne
    private TabType tabType;

    public BusinessDTO(Business business) {
   }

    public BusinessDTO(Long id, String name, LocalDate createdDate, LocalDate modifiedDate, Integer pisition, String icon, String userName, String jobPosition, String companyName, String companySite, String email, String phone, String address, String twitter, String facebook, String google, String linkedIn, String photo, String mainColor, String color, TabType tabType) {
        this.id = id;
        this.name = name;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.pisition = pisition;
        this.icon = icon;
        this.userName = userName;
        this.jobPosition = jobPosition;
        this.companyName = companyName;
        this.companySite = companySite;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.twitter = twitter;
        this.facebook = facebook;
        this.google = google;
        this.linkedIn = linkedIn;
        this.photo = photo;
        this.mainColor = mainColor;
        this.color = color;
        this.tabType = tabType;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDate getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(LocalDate modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Integer getPisition() {
        return pisition;
    }

    public void setPisition(Integer pisition) {
        this.pisition = pisition;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getJobPosition() {
        return jobPosition;
    }

    public void setJobPosition(String jobPosition) {
        this.jobPosition = jobPosition;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanySite() {
        return companySite;
    }

    public void setCompanySite(String companySite) {
        this.companySite = companySite;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getGoogle() {
        return google;
    }

    public void setGoogle(String google) {
        this.google = google;
    }

    public String getLinkedIn() {
        return linkedIn;
    }

    public void setLinkedIn(String linkedIn) {
        this.linkedIn = linkedIn;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getMainColor() {
        return mainColor;
    }

    public void setMainColor(String mainColor) {
        this.mainColor = mainColor;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public TabType getTabType() {
        return tabType;
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
        BusinessDTO business = (BusinessDTO) o;
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
