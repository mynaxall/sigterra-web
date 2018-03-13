package itomy.sigterra.web.rest.vm;


import itomy.sigterra.domain.enumeration.CardletFooterIndex;
import itomy.sigterra.repository.CardletRepository;
import itomy.sigterra.web.rest.vm.CardletBackgroundVM;
import itomy.sigterra.web.rest.vm.CardletFooterVM;
import itomy.sigterra.web.rest.vm.CardletHeaderVM;
import org.springframework.validation.annotation.Validated;

import javax.inject.Inject;
import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
public class CardletViewRequestResponseVM {
    @NotNull
    private Long cardletId;

    @Valid
    private CardletHeaderVM headers;

    @Valid
    private CardletBackgroundVM background;

    @Valid
    private List<CardletFooterVM> footers;

    public CardletViewRequestResponseVM() {
        // For Jackson
    }

    public CardletViewRequestResponseVM(Long cardletId, CardletHeaderVM headers, CardletBackgroundVM background, List<CardletFooterVM> footers) {
        this.cardletId = cardletId;
        this.headers = headers;
        this.background = background;
        this.footers = footers;
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

    public List<CardletFooterVM> getFooters() {
        return footers;
    }

    public void setFooters(List<CardletFooterVM> footers) {
        this.footers = footers;
    }

    @Override
    public String toString() {
        return "CardletViewRequestResponseVM{" +
            "cardletId=" + cardletId +
            ", headers=" + headers +
            ", background=" + background +
            ", footers=" + footers +
            '}';
    }
}
