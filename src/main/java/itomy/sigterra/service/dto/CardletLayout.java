package itomy.sigterra.service.dto;

import java.io.Serializable;

/**
 * Created by alexander on 1/31/17.
 */
public class CardletLayout implements Serializable {
    private String url;
    private String mainColor;
    private String secondaryColor;
    private Long tabId;

    public CardletLayout() {
    }

    public CardletLayout(String url, String mainColor, String secondaryColor, Long tabId) {
        this.url = url;
        this.mainColor = mainColor;
        this.secondaryColor = secondaryColor;
        this.tabId = tabId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMainColor() {
        return mainColor;
    }

    public void setMainColor(String mainColor) {
        this.mainColor = mainColor;
    }

    public String getSecondaryColor() {
        return secondaryColor;
    }

    public void setSecondaryColor(String secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    public Long getTabId() {
        return tabId;
    }

    public void setTabId(Long tabId) {
        this.tabId = tabId;
    }

    @Override
    public String toString() {
        return "CardletLayout{" +
            "url='" + url + '\'' +
            ", mainColor='" + mainColor + '\'' +
            ", secondaryColor='" + secondaryColor + '\'' +
            ", tabId=" + tabId +
            '}';
    }
}
