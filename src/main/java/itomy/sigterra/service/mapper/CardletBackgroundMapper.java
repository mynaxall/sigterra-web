package itomy.sigterra.service.mapper;

import itomy.sigterra.domain.CardletBackground;
import itomy.sigterra.web.rest.vm.CardletBackgroundVM;

public class CardletBackgroundMapper {
    public static void map(CardletBackgroundVM source, CardletBackground destanation) {
        destanation.setImage(source.getImageUrl());
        destanation.setCaptionText(source.getText());
        destanation.setTextColor(source.isTextColor());
    }

    public static void map(CardletBackground source, CardletBackgroundVM destanation) {
        destanation.setId(source.getId());
        destanation.setImageUrl(source.getImage());
        destanation.setText(source.getCaptionText());
        destanation.setTextColor(source.isTextColor());
    }
}
