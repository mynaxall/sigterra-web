package itomy.sigterra.service.dto;

import java.io.Serializable;

/**
 * Created by alexander on 1/31/17.
 */
public class CardletLayout implements Serializable {
    private String url;
    private String mainColor;
    private String secondaryColor;

    public CardletLayout() {
    }

    public CardletLayout(String url, String mainColor, String secondaryColor) {
        this.url = url;
        this.mainColor = mainColor;
        this.secondaryColor = secondaryColor;
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

    @Override
    public String toString() {
        return "CardletLayout{" +
            "url='" + url + '\'' +
            ", mainColor='" + mainColor + '\'' +
            ", secondaryColor='" + secondaryColor + '\'' +
            '}';
    }
}
