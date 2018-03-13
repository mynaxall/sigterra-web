package itomy.sigterra.web.rest.vm;

import itomy.sigterra.domain.enumeration.CardletFooterIndex;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Size;

@Validated
public class CardletFooterVM {
    private Long id;

    private CardletFooterIndex index;

    @Size(max = 25)
    private String name;

    @Size(max = 25)
    private String url;

    @Size(max = 255)
    private String logoUrl;

    public CardletFooterVM() {
        //For Jackson
    }

    public CardletFooterVM(Long id, CardletFooterIndex index, String name, String url, String logo) {
        this.id = id;
        this.index = index;
        this.name = name;
        this.url = url;
        this.logoUrl = logo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CardletFooterIndex getIndex() {
        return index;
    }

    public void setIndex(CardletFooterIndex index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    @Override
    public String toString() {
        return "CardletFooterVM{" +
            "id=" + id +
            ", index=" + index +
            ", name='" + name + '\'' +
            ", url='" + url + '\'' +
            ", logoUrl='" + logoUrl + '\'' +
            '}';
    }
}
