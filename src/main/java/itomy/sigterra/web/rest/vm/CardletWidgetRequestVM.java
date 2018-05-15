package itomy.sigterra.web.rest.vm;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public class CardletWidgetRequestVM {

    @NotNull
    private Long cardletId;

    @Valid
    private List<CardletTestimonialWidgetRequestVM> cardletTestimonialWidget;

    @Valid
    private List<CardletQuickBitesWidgetRequestVM> cardletQuickBitesWidget;

    @Valid
    private List<CardletContentLibraryWidgetRequestVM> cardletContentLibraryWidget;

    public CardletWidgetRequestVM() {
        // For Jackson
    }

    public CardletWidgetRequestVM(Long cardletId,
                                  List<CardletTestimonialWidgetRequestVM> cardletTestimonialWidget,
                                  List<CardletQuickBitesWidgetRequestVM> cardletQuickBitesWidget,
                                  List<CardletContentLibraryWidgetRequestVM> cardletContentLibraryWidget) {
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

    public List<CardletTestimonialWidgetRequestVM> getCardletTestimonialWidget() {
        return cardletTestimonialWidget;
    }

    public void setCardletTestimonialWidget(List<CardletTestimonialWidgetRequestVM> cardletTestimonialWidget) {
        this.cardletTestimonialWidget = cardletTestimonialWidget;
    }

    public List<CardletQuickBitesWidgetRequestVM> getCardletQuickBitesWidget() {
        return cardletQuickBitesWidget;
    }

    public void setCardletQuickBitesWidget(List<CardletQuickBitesWidgetRequestVM> cardletQuickBitesWidget) {
        this.cardletQuickBitesWidget = cardletQuickBitesWidget;
    }

    public List<CardletContentLibraryWidgetRequestVM> getCardletContentLibraryWidget() {
        return cardletContentLibraryWidget;
    }

    public void setCardletContentLibraryWidget(List<CardletContentLibraryWidgetRequestVM> cardletContentLibraryWidget) {
        this.cardletContentLibraryWidget = cardletContentLibraryWidget;
    }
}
