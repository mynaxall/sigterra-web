package itomy.sigterra.web.rest.vm;

import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
public class CardletWidgetResponseVM {

    @NotNull
    private Long cardletId;

    @Valid
    private List<CardletTestimonialWidgetResponseVM> cardletTestimonialWidget;

    @Valid
    private List<CardletQuickBitesWidgetResponseVM> cardletQuickBitesWidget;

    @Valid
    private List<CardletContentLibraryWidgetResponseVM> cardletContentLibraryWidget;

    public CardletWidgetResponseVM() {
        // For Jackson
    }

    public CardletWidgetResponseVM(Long cardletId,
                                   List<CardletTestimonialWidgetResponseVM> cardletTestimonialWidget,
                                   List<CardletQuickBitesWidgetResponseVM> cardletQuickBitesWidget,
                                   List<CardletContentLibraryWidgetResponseVM> cardletContentLibraryWidget) {
        this.cardletId = cardletId;
        this.cardletTestimonialWidget = cardletTestimonialWidget;
        this.cardletQuickBitesWidget = cardletQuickBitesWidget;
        this.cardletContentLibraryWidget = cardletContentLibraryWidget;
    }

    public Long getCardletId() {
        return cardletId;
    }

    public void setCardletId(Long cardletId) {
        this.cardletId = cardletId;
    }

    public List<CardletTestimonialWidgetResponseVM> getCardletTestimonialWidget() {
        return cardletTestimonialWidget;
    }

    public void setCardletTestimonialWidget(List<CardletTestimonialWidgetResponseVM> cardletTestimonialWidget) {
        this.cardletTestimonialWidget = cardletTestimonialWidget;
    }

    public List<CardletQuickBitesWidgetResponseVM> getCardletQuickBitesWidget() {
        return cardletQuickBitesWidget;
    }

    public void setCardletQuickBitesWidget(List<CardletQuickBitesWidgetResponseVM> cardletQuickBitesWidget) {
        this.cardletQuickBitesWidget = cardletQuickBitesWidget;
    }

    public List<CardletContentLibraryWidgetResponseVM> getCardletContentLibraryWidget() {
        return cardletContentLibraryWidget;
    }

    public void setCardletContentLibraryWidget(List<CardletContentLibraryWidgetResponseVM> cardletContentLibraryWidget) {
        this.cardletContentLibraryWidget = cardletContentLibraryWidget;
    }
}
