package itomy.sigterra.web.rest.vm;

import itomy.sigterra.domain.CardletFooter;
import itomy.sigterra.domain.enumeration.CardletFooterIndex;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Validated
public class CardletFooterVM {
    private Long id;

    @NotNull
    private CardletFooterIndex position;

    @Size(max = 25)
    private String name;

    @Size(max = 25)
    private String url;

    @Size(max = 255)
    private String logoUrl;

    @Size(max=255)
    private String title;

    public CardletFooterVM() {
        //For Jackson
    }

    public CardletFooterVM(Long id, CardletFooterIndex position, String name, String url, String logoUrl, String title) {
        this.id = id;
        this.position = position;
        this.name = name;
        this.url = url;
        this.logoUrl = logoUrl;
        this.title = title;
    }

    public CardletFooterVM(CardletFooter cardletFooter) {
        mapFromCardletFooter(cardletFooter);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CardletFooterIndex getPosition() {
        return position;
    }

    public void setPosition(CardletFooterIndex position) {
        this.position = position;
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
            ", position=" + position +
            ", name='" + name + '\'' +
            ", url='" + url + '\'' +
            ", logoUrl='" + logoUrl + '\'' +
            '}';
    }

    public void mapToCardletFooter(CardletFooter cardletFooterEntity){
        cardletFooterEntity.setPosition(this.getPosition());
        cardletFooterEntity.setLogo(this.getLogoUrl());
        cardletFooterEntity.setName(this.getName());
        cardletFooterEntity.setUrl(this.getUrl());
        cardletFooterEntity.setTitle(this.getTitle());
    }

    public void mapFromCardletFooter(CardletFooter cardletFooterEntity){
        this.id = cardletFooterEntity.getId();
        this.position = cardletFooterEntity.getPosition();
        this.name = cardletFooterEntity.getName();
        this.url = cardletFooterEntity.getUrl();
        this.logoUrl = cardletFooterEntity.getLogo();
        this.title = cardletFooterEntity.getTitle();
    }
}
