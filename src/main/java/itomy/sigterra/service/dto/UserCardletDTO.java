package itomy.sigterra.service.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by alexander on 1/31/17.
 */
public class UserCardletDTO implements Serializable {
    private String cardletName;
    private Long id;
    private List<CardletTab> tabs;
    private List<Long> removeTabs;
    private List<Long> removeItems;
    private List<Long> removeBusiness;


    public UserCardletDTO() {
    }

    public UserCardletDTO(String cardletName, Long id, List<CardletTab> tabs, List<Long> removeTabs, List<Long> removeItems, List<Long> removeBusiness) {
        this.cardletName = cardletName;
        this.id = id;
        this.tabs = tabs;
        this.removeTabs = removeTabs;
        this.removeItems = removeItems;
        this.removeBusiness = removeBusiness;
    }

    public String getCardletName() {
        return cardletName;
    }

    public void setCardletName(String cardletName) {
        this.cardletName = cardletName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<CardletTab> getTabs() {
        return tabs;
    }

    public void setTabs(List<CardletTab> tabs) {
        this.tabs = tabs;
    }

    public List<Long> getRemoveTabs() {
        return removeTabs;
    }

    public void setRemoveTabs(List<Long> removeTabs) {
        this.removeTabs = removeTabs;
    }

    public List<Long> getRemoveItems() {
        return removeItems;
    }

    public void setRemoveItems(List<Long> removeItems) {
        this.removeItems = removeItems;
    }

    public List<Long> getRemoveBusiness() {
        return removeBusiness;
    }

    public void setRemoveBusiness(List<Long> removeBusiness) {
        this.removeBusiness = removeBusiness;
    }

    @Override
    public String toString() {
        return "UserCardletDTO{" +
            "cardletName='" + cardletName + '\'' +
            ", id=" + id +
            ", tabs=" + tabs +
            ", removeTabs=" + removeTabs +
            ", removeItems=" + removeItems +
            ", removeBusiness=" + removeBusiness +
            '}';
    }
}
