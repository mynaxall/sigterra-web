package itomy.sigterra.web.rest.vm;

import itomy.sigterra.config.Constants;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.Size;

public class CardletHeaderVM {
    private Long id;

    @Size(max = 6)
    private String ctaColor;

    @Size(max = 20)
    private String text;

    private String logoUrl;

    private String photoUrl;

    @Size(max = 50)
    private String name;

    private String title;

    @Size(max = 100)
    private String company;

    @Size(max = 50)
    private String phone;

    @Email(regexp = Constants.LOGIN_REGEX)
    @Size(max = 100)
    private String email;

    public CardletHeaderVM() {
        //For Jackson
    }

    public CardletHeaderVM(Long id, String ctaColor, String text, String logoUrl, String photoUrl, String name, String title, String company, String phone, String email) {
        this.id = id;
        this.ctaColor = ctaColor;
        this.text = text;
        this.logoUrl = logoUrl;
        this.photoUrl = photoUrl;
        this.name = name;
        this.title = title;
        this.company = company;
        this.phone = phone;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCtaColor() {
        return ctaColor;
    }

    public void setCtaColor(String ctaColor) {
        this.ctaColor = ctaColor;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
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

    @Override
    public String toString() {
        return "CardletHeaderVM{" +
            "id=" + id +
            ", ctaColor='" + ctaColor + '\'' +
            ", text='" + text + '\'' +
            ", logoUrl='" + logoUrl + '\'' +
            ", photoUrl='" + photoUrl + '\'' +
            ", name='" + name + '\'' +
            ", title='" + title + '\'' +
            ", company='" + company + '\'' +
            ", phone='" + phone + '\'' +
            ", email='" + email + '\'' +
            '}';
    }
}
