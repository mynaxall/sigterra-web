package itomy.sigterra.service.dto;

/**
 * Created by alexander on 1/31/17.
 */
public class CardletBusinessTab extends CardletTab {
    private InputModel userName;
    private InputModel userEmail;
    private InputModel phone;
    private InputModel address;
    private InputModel company;
    private InputModel site;
    private InputModel job;
    private BusinessSocialLinks socialLinks;

    public CardletBusinessTab() {
        super();
    }


//    public InputModel getUserName() {
//        return userName;
//    }
//
//    public void setUserName(InputModel userName) {
//        this.userName = userName;
//    }
//
//    public InputModel getUserEmail() {
//        return userEmail;
//    }
//
//    public void setUserEmail(InputModel userEmail) {
//        this.userEmail = userEmail;
//    }
//
//    public InputModel getPhone() {
//        return phone;
//    }
//
//    public void setPhone(InputModel phone) {
//        this.phone = phone;
//    }
//
//    public InputModel getAddress() {
//        return address;
//    }
//
//    public void setAddress(InputModel address) {
//        this.address = address;
//    }
//
//    public InputModel getCompany() {
//        return company;
//    }
//
//    public void setCompany(InputModel company) {
//        this.company = company;
//    }
//
//    public InputModel getSite() {
//        return site;
//    }
//
//    public void setSite(InputModel site) {
//        this.site = site;
//    }
//
//    public InputModel getJob() {
//        return job;
//    }
//
//    public void setJob(InputModel job) {
//        this.job = job;
//    }
//
//    public BusinessSocialLinks getSocialLinks() {
//        return socialLinks;
//    }
//
//    public void setSocialLinks(BusinessSocialLinks socialLinks) {
//        this.socialLinks = socialLinks;
//    }

    @Override
    public String toString() {
        return "CardletBusinessTab{" +
            "userName=" + userName +
            ", userEmail=" + userEmail +
            ", phone=" + phone +
            ", address=" + address +
            ", company=" + company +
            ", site=" + site +
            ", job=" + job +
            ", socialLinks=" + socialLinks +
            '}';
    }
}
