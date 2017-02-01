package itomy.sigterra.service.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by alexander on 1/31/17.
 */
public class CardletTab implements Serializable {
    private String name;
    private Integer position;
    private Integer tabType;
    private CardletLayout layout;
    private List<ItemModel> items;
    private InputModel userName;
    private InputModel userEmail;
    private InputModel phone;
    private InputModel address;
    private InputModel company;
    private InputModel site;
    private InputModel job;
    private BusinessSocialLinks socialLinks;

    public CardletTab() {
    }

    public CardletTab(String name, Integer position, Integer tabType, CardletLayout layout, List<ItemModel> items, InputModel userName, InputModel userEmail, InputModel phone, InputModel address, InputModel company, InputModel site, InputModel job, BusinessSocialLinks socialLinks) {
        this.name = name;
        this.position = position;
        this.tabType = tabType;
        this.layout = layout;
        this.items = items;
        this.userName = userName;
        this.userEmail = userEmail;
        this.phone = phone;
        this.address = address;
        this.company = company;
        this.site = site;
        this.job = job;
        this.socialLinks = socialLinks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getTabType() {
        return tabType;
    }

    public void setTabType(Integer tabType) {
        this.tabType = tabType;
    }

    public CardletLayout getLayout() {
        return layout;
    }

    public void setLayout(CardletLayout layout) {
        this.layout = layout;
    }

    public List<ItemModel> getItems() {
        return items;
    }

    public void setItems(List<ItemModel> items) {
        this.items = items;
    }

    public InputModel getUserName() {
        return userName;
    }

    public void setUserName(InputModel userName) {
        this.userName = userName;
    }

    public InputModel getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(InputModel userEmail) {
        this.userEmail = userEmail;
    }

    public InputModel getPhone() {
        return phone;
    }

    public void setPhone(InputModel phone) {
        this.phone = phone;
    }

    public InputModel getAddress() {
        return address;
    }

    public void setAddress(InputModel address) {
        this.address = address;
    }

    public InputModel getCompany() {
        return company;
    }

    public void setCompany(InputModel company) {
        this.company = company;
    }

    public InputModel getSite() {
        return site;
    }

    public void setSite(InputModel site) {
        this.site = site;
    }

    public InputModel getJob() {
        return job;
    }

    public void setJob(InputModel job) {
        this.job = job;
    }

    public BusinessSocialLinks getSocialLinks() {
        return socialLinks;
    }

    public void setSocialLinks(BusinessSocialLinks socialLinks) {
        this.socialLinks = socialLinks;
    }

    @Override
    public String toString() {
        return "CardletTab{" +
            "name='" + name + '\'' +
            ", position=" + position +
            ", tabType=" + tabType +
            ", layout=" + layout +
            ", items=" + items +
            ", userName=" + userName +
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
