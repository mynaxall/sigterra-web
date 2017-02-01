package itomy.sigterra.service.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by alexander on 1/31/17.
 */
public class UserCardletDTO implements Serializable {
    private String cardletName;
    private int id;
    private List<CardletTab> tabs;

    public UserCardletDTO() {
    }

    public UserCardletDTO(String cardletName, int id, List<CardletTab> tabs) {
        this.cardletName = cardletName;
        this.id = id;
        this.tabs = tabs;
    }

    public String getCardletName() {
        return cardletName;
    }

    public void setCardletName(String cardletName) {
        this.cardletName = cardletName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<CardletTab> getTabs() {
        return tabs;
    }

    public void setTabs(List<CardletTab> tabs) {
        this.tabs = tabs;
    }

    @Override
    public String toString() {
        return "UserCardletDTO{" +
            "cardletName='" + cardletName + '\'' +
            ", id=" + id +
            ", tabs=" + tabs +
            '}';
    }
}
