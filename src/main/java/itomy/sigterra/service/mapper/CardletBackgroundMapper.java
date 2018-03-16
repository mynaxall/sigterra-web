package itomy.sigterra.service.mapper;

import itomy.sigterra.domain.CardletBackground;
import itomy.sigterra.web.rest.vm.CardletBackgroundVM;

public class CardletBackgroundMapper {
    public static void mapToEntity(CardletBackgroundVM source, CardletBackground destination) {
        if (source == null || destination == null) {
            new IllegalArgumentException();
        }
        destination.setImage(source.getImageUrl());
        destination.setCaptionText(source.getText());
        destination.setTextColor(source.isTextColor());
    }

    public static CardletBackgroundVM mapFromEntity(CardletBackground source) {
        if (source == null) {
            return null;
        }
        CardletBackgroundVM destination = new CardletBackgroundVM();
        destination.setId(source.getId());
        destination.setImageUrl(source.getImage());
        destination.setText(source.getCaptionText());
        destination.setTextColor(source.isTextColor());
        return destination;
    }
}
