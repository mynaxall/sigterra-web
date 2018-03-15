package itomy.sigterra.web.rest.vm;


import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Validated
public class CardletViewRequestResponseVM {
    @NotNull
    private Long cardletId;

    @Valid
    private CardletHeaderVM header;

    @Valid
    private CardletBackgroundVM background;

    @Valid
    private CardletLinksVM links;

    @Valid
    private CardletFooterVM footer;

    public CardletViewRequestResponseVM() {
        // For Jackson
    }

    public CardletViewRequestResponseVM(Long cardletId, CardletHeaderVM header, CardletBackgroundVM background, CardletLinksVM links) {
        this.cardletId = cardletId;
        this.header = header;
        this.background = background;
        this.links = links;
    }

    public Long getCardletId() {
        return cardletId;
    }

    public void setCardletId(Long cardletId) {
        this.cardletId = cardletId;
    }

    public CardletHeaderVM getHeader() {
        return header;
    }

    public void setHeader(CardletHeaderVM header) {
        this.header = header;
    }

    public CardletBackgroundVM getBackground() {
        return background;
    }

    public void setBackground(CardletBackgroundVM background) {
        this.background = background;
    }

    public CardletLinksVM getLinks() {
        return links;
    }

    public void setLinks(CardletLinksVM links) {
        this.links = links;
    }

    @Override
    public String toString() {
        return "CardletViewRequestResponseVM{" +
            "cardletId=" + cardletId +
            ", headers=" + header +
            ", background=" + background +
            ", links=" + links +
            '}';
    }

    public CardletFooterVM getFooter() {
        return footer;
    }

    public void setFooter(CardletFooterVM footer) {
        this.footer = footer;
    }

}
