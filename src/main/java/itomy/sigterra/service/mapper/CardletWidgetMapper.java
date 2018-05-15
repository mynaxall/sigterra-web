package itomy.sigterra.service.mapper;

import itomy.sigterra.domain.Cardlet;
import itomy.sigterra.domain.CardletContentLibraryWidget;
import itomy.sigterra.domain.CardletQuickBitesWidget;
import itomy.sigterra.domain.CardletTestimonialWidget;
import itomy.sigterra.web.rest.vm.CardletContentLibraryWidgetRequestVM;
import itomy.sigterra.web.rest.vm.CardletQuickBitesWidgetRequestVM;
import itomy.sigterra.web.rest.vm.CardletTestimonialWidgetRequestVM;

public class CardletWidgetMapper {

    public static CardletTestimonialWidget mapToEntity(CardletTestimonialWidgetRequestVM vm, Cardlet cardlet) {
        CardletTestimonialWidget entity = new CardletTestimonialWidget();

        entity.setId(vm.getId());
        entity.setName(vm.getName());
        entity.setCoName(vm.getCoName());
        entity.setDesignation(vm.getDesignation());
        entity.setDescription(vm.getDescription());
        entity.setCardlet(cardlet);

        return entity;
    }

    public static CardletContentLibraryWidget mapToEntity(CardletContentLibraryWidgetRequestVM vm, Cardlet cardlet) {
        CardletContentLibraryWidget entity = new CardletContentLibraryWidget();

        entity.setId(vm.getId());
        entity.setTitle(vm.getTitle());
        entity.setUploadFileUrl(vm.getUploadFileUrl());
        entity.setCoverImageUrl(vm.getCoverImageUrl());
        entity.setCardlet(cardlet);

        return entity;
    }

    public static CardletQuickBitesWidget mapToEntity(CardletQuickBitesWidgetRequestVM vm, Cardlet cardlet) {
        CardletQuickBitesWidget entity = new CardletQuickBitesWidget();

        entity.setId(vm.getId());
        entity.setTitle(vm.getTitle());
        entity.setDescription(vm.getDescription());
        entity.setCardlet(cardlet);

        return entity;
    }
}
