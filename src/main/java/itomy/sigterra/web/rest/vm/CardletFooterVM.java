package itomy.sigterra.web.rest.vm;

import itomy.sigterra.domain.CardletFooter;
import itomy.sigterra.service.mapper.CardletFooterMapper;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Size;

@Validated
public class CardletFooterVM implements VmWithLongId {
    private Long id;
    @Size(max = 255)
    private String title;
    @Size(max = 255)
    private String facebookLink;
    @Size(max = 255)
    private String twitterLink;
    @Size(max = 255)
    private String linkedinLink;

    public CardletFooterVM(Long id, String title, String facebookLink, String twitterLink, String linkedinLink) {

        this.id = id;
        this.title = title;
        this.facebookLink = facebookLink;
        this.twitterLink = twitterLink;
        this.linkedinLink = linkedinLink;
    }

    public CardletFooterVM() {

    }

    public CardletFooterVM(CardletFooter cardletFooter) {
        CardletFooterMapper.map(cardletFooter, this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFacebookLink() {
        return facebookLink;
    }

    public void setFacebookLink(String facebookLink) {
        this.facebookLink = facebookLink;
    }

    public String getTwitterLink() {
        return twitterLink;
    }

    public void setTwitterLink(String twitterLink) {
        this.twitterLink = twitterLink;
    }

    public String getLinkedinLink() {
        return linkedinLink;
    }

    public void setLinkedinLink(String linkedinLink) {
        this.linkedinLink = linkedinLink;
    }

    @Override
    public String toString() {
        return "CardletFooterVM{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", facebookLink='" + facebookLink + '\'' +
            ", twitterLink='" + twitterLink + '\'' +
            ", linkedinLink='" + linkedinLink + '\'' +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CardletFooterVM that = (CardletFooterVM) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (facebookLink != null ? !facebookLink.equals(that.facebookLink) : that.facebookLink != null) return false;
        if (twitterLink != null ? !twitterLink.equals(that.twitterLink) : that.twitterLink != null) return false;
        return linkedinLink != null ? linkedinLink.equals(that.linkedinLink) : that.linkedinLink == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (facebookLink != null ? facebookLink.hashCode() : 0);
        result = 31 * result + (twitterLink != null ? twitterLink.hashCode() : 0);
        result = 31 * result + (linkedinLink != null ? linkedinLink.hashCode() : 0);
        return result;
    }



}
