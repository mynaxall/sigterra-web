package itomy.sigterra.service.mapper;

import itomy.sigterra.domain.Cardlet;
import itomy.sigterra.domain.CardletQuickBitesWidget;
import itomy.sigterra.domain.CardletTestimonialWidget;
import itomy.sigterra.web.rest.vm.CardletQuickBitesWidgetRequestVM;
import itomy.sigterra.web.rest.vm.CardletTestimonialWidgetRequestVM;

public class CardletWidgetMapper {

    public static CardletTestimonialWidget mapToEntity(CardletTestimonialWidgetRequestVM cardletTestimonialWidgetRequestVM, Cardlet cardlet) {
        CardletTestimonialWidget cardletTestimonialWidget = new CardletTestimonialWidget();

        cardletTestimonialWidget.setName(cardletTestimonialWidgetRequestVM.getName());
        cardletTestimonialWidget.setCoName(cardletTestimonialWidgetRequestVM.getCoName());
        cardletTestimonialWidget.setDesignation(cardletTestimonialWidgetRequestVM.getDesignation());
        cardletTestimonialWidget.setDescription(cardletTestimonialWidgetRequestVM.getDescription());
        cardletTestimonialWidget.setCardlet(cardlet);

        return cardletTestimonialWidget;
    }

    public static CardletQuickBitesWidget mapToEntity(CardletQuickBitesWidgetRequestVM vm, Cardlet cardlet) {
        CardletQuickBitesWidget cardletQuickBitesWidget = new CardletQuickBitesWidget();
        cardletQuickBitesWidget.setTitle(vm.getTitle());
        cardletQuickBitesWidget.setDescription(vm.getDescription());
        cardletQuickBitesWidget.setCardlet(cardlet);

        return cardletQuickBitesWidget;
    }
}
