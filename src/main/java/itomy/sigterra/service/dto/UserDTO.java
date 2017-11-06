package itomy.sigterra.service.dto;

import itomy.sigterra.config.Constants;
import itomy.sigterra.domain.Authority;
import itomy.sigterra.domain.User;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A DTO representing a user, with his authorities.
 */
public class UserDTO {

    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    private String login;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @NotEmpty
    @Size(min = 1, max = 30)
    private String username;

    private String phoneNumber;

    private String address;

    private String companyName;

    private String companySite;

    private String jobTitle;

    @Email(regexp = Constants.LOGIN_REGEX)
    @Size(min = 5, max = 100)
    private String email;

    private boolean activated = false;

    @Size(min = 2, max = 5)
    private String langKey;

    private Set<String> authorities;

    private String imageUrl;

    public UserDTO() {
    }

    public UserDTO(User user) {
        this(user.getLogin(), user.getFirstName(), user.getLastName(),
             user.getEmail(), user.getActivated(), user.getLangKey(),
             user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toSet()),
             user.getUsername(), user.getPhoneNumber(),
             user.getAddress(), user.getCompanyName(), user.getCompanySite(), user.getJobTitle(), user.getImageUrl());
    }

    public UserDTO(String login, String firstName, String lastName,
                   String email, boolean activated, String langKey, Set<String> authorities,
                   String username, String phoneNumber, String address, String companyName,
                   String companySite, String jobTitle, String imageUrl) {

        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.activated = activated;
        this.langKey = langKey;
        this.authorities = authorities;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.companyName = companyName;
        this.companySite = companySite;
        this.jobTitle = jobTitle;
        this.imageUrl = imageUrl;
    }


    public String getLogin() {
        return login;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public boolean isActivated() {
        return activated;
    }

    public String getLangKey() {
        return langKey;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    public String getUsername() {
        return username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCompanySite() {
        return companySite;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
            "login='" + login + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", username='" + username + '\'' +
            ", phoneNumber='" + phoneNumber + '\'' +
            ", address='" + address + '\'' +
            ", companyName='" + companyName + '\'' +
            ", companySite='" + companySite + '\'' +
            ", jobTitle='" + jobTitle + '\'' +
            ", email='" + email + '\'' +
            ", activated=" + activated +
            ", langKey='" + langKey + '\'' +
            ", authorities=" + authorities +
            ", imageUrl='" + imageUrl + '\'' +
            '}';
    }
}
