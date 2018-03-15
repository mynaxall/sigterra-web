package itomy.sigterra.web.rest.vm;


import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Validated
public class CardletViewRequestResponseVM {
    @NotNull
    private Long cardletId;

    @Valid
    private CardletHeaderVM headers;

    @Valid
    private CardletBackgroundVM background;

    @Valid
    private CardletLinksVM link;

    @Valid
    private CardletFooterVM footer;

    public CardletViewRequestResponseVM() {
        // For Jackson
    }

    public CardletViewRequestResponseVM(Long cardletId, CardletHeaderVM headers, CardletBackgroundVM background, CardletLinksVM link) {
        this.cardletId = cardletId;
        this.headers = headers;
        this.background = background;
        this.link = link;
    }

    public Long getCardletId() {
        return cardletId;
    }

    public void setCardletId(Long cardletId) {
        this.cardletId = cardletId;
    }

    public CardletHeaderVM getHeaders() {
        return headers;
    }

    public void setHeaders(CardletHeaderVM headers) {
        this.headers = headers;
    }

    public CardletBackgroundVM getBackground() {
        return background;
    }

    public void setBackground(CardletBackgroundVM background) {
        this.background = background;
    }

    public CardletLinksVM getLinks() {
        return link;
    }

    public void setLink(CardletLinksVM link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return "CardletViewRequestResponseVM{" +
            "cardletId=" + cardletId +
            ", headers=" + headers +
            ", background=" + background +
            ", link=" + link +
            '}';
    }

    public CardletFooterVM getFooter() {
        return footer;
    }

    public void setFooter(CardletFooterVM footer) {
        this.footer = footer;
    }

}
