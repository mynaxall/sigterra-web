package itomy.sigterra.service.mapper;

import itomy.sigterra.domain.Cardlet;
import itomy.sigterra.domain.CardletTestimonialWidget;
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
}
