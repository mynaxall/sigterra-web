package itomy.sigterra.service.mapper;

import itomy.sigterra.domain.CardletHeader;
import itomy.sigterra.web.rest.vm.CardletHeaderVM;

public class CardletHeaderMapper {
    public static void mapToEntity(CardletHeaderVM source, CardletHeader destination) {
        if (source == null || destination == null) {
            new IllegalArgumentException();
        }
        destination.setCtaButtonColor(source.getCtaColor());
        destination.setCtaText(source.getText());
        destination.setLogo(source.getLogoUrl());
        destination.setPhoto(source.getPhotoUrl());
        destination.setName(source.getName());
        destination.setTitle(source.getTitle());
        destination.setPhone(source.getPhone());
        destination.setEmail(source.getEmail());

    }

    public static CardletHeaderVM mapFromEntity(CardletHeader source) {
        if (source == null) {
            return null;
        }
        CardletHeaderVM destination = new CardletHeaderVM();
        destination.setId(source.getId());
        destination.setCtaColor(source.getCtaButtonColor());
        destination.setText(source.getCtaText());
        destination.setLogoUrl(source.getLogo());
        destination.setPhotoUrl(source.getPhoto());
        destination.setName(source.getName());
        destination.setTitle(source.getTitle());
        destination.setPhone(source.getPhone());
        destination.setEmail(source.getEmail());
        return destination;
    }
}
