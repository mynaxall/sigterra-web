package itomy.sigterra.service.dto;

import itomy.sigterra.domain.InputProperties;

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
    private InputProperties userName;
    private InputProperties userEmail;
    private InputProperties phone;
    private InputProperties address;
    private InputProperties company;
    private InputProperties site;
    private InputProperties job;
    private BusinessSocialLinks socialLinks;

    public CardletTab() {
    }

    public CardletTab(String name, Integer position, Integer tabType, CardletLayout layout, List<ItemModel> items, InputProperties userName, InputProperties userEmail, InputProperties phone, InputProperties address, InputProperties company, InputProperties site, InputProperties job, BusinessSocialLinks socialLinks) {
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

    public InputProperties getUserName() {
        return userName;
    }

    public void setUserName(InputProperties userName) {
        this.userName = userName;
    }

    public InputProperties getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(InputProperties userEmail) {
        this.userEmail = userEmail;
    }

    public InputProperties getPhone() {
        return phone;
    }

    public void setPhone(InputProperties phone) {
        this.phone = phone;
    }

    public InputProperties getAddress() {
        return address;
    }

    public void setAddress(InputProperties address) {
        this.address = address;
    }

    public InputProperties getCompany() {
        return company;
    }

    public void setCompany(InputProperties company) {
        this.company = company;
    }

    public InputProperties getSite() {
        return site;
    }

    public void setSite(InputProperties site) {
        this.site = site;
    }

    public InputProperties getJob() {
        return job;
    }

    public void setJob(InputProperties job) {
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
