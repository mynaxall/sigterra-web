package itomy.sigterra.web.rest.vm;

import itomy.sigterra.domain.CardletLinks;
import itomy.sigterra.service.mapper.CardletLinksMapper;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Size;

@Validated
public class CardletLinksVM {
    private Long id;

    @Size(max = 30)
    private String title;

    @Size(max = 20)
    private String name1;
    @Size(max = 255)
    private String url1;
    @Size(max = 255)
    private String logoUrl1;

    @Size(max = 20)
    private String name2;
    @Size(max = 255)
    private String url2;
    @Size(max = 255)
    private String logoUrl2;

    @Size(max = 20)
    private String name3;
    @Size(max = 255)
    private String url3;
    @Size(max = 255)
    private String logoUrl3;

    public CardletLinksVM() {
        //For Jackson
    }

    public CardletLinksVM(CardletLinks cardletLinks) {
        CardletLinksMapper.map(cardletLinks, this);
    }

    public CardletLinksVM(Long id, String name1, String url1, String logoUrl1, String name2, String url2, String logoUrl2, String name3, String url3, String logoUrl3, String title) {
        this.id = id;
        this.name1 = name1;
        this.url1 = url1;
        this.logoUrl1 = logoUrl1;
        this.name2 = name2;
        this.url2 = url2;
        this.logoUrl2 = logoUrl2;
        this.name3 = name3;
        this.url3 = url3;
        this.logoUrl3 = logoUrl3;
        this.title = title;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getUrl1() {
        return url1;
    }

    public void setUrl1(String url1) {
        this.url1 = url1;
    }

    public String getLogoUrl1() {
        return logoUrl1;
    }

    public void setLogoUrl1(String logoUrl1) {
        this.logoUrl1 = logoUrl1;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public String getUrl2() {
        return url2;
    }

    public void setUrl2(String url2) {
        this.url2 = url2;
    }

    public String getLogoUrl2() {
        return logoUrl2;
    }

    public void setLogoUrl2(String logoUrl2) {
        this.logoUrl2 = logoUrl2;
    }

    public String getName3() {
        return name3;
    }

    public void setName3(String name3) {
        this.name3 = name3;
    }

    public String getUrl3() {
        return url3;
    }

    public void setUrl3(String url3) {
        this.url3 = url3;
    }

    public String getLogoUrl3() {
        return logoUrl3;
    }

    public void setLogoUrl3(String logoUrl3) {
        this.logoUrl3 = logoUrl3;
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

}
