package itomy.sigterra.service.mapper;

import itomy.sigterra.domain.CardletFooter;
import itomy.sigterra.web.rest.vm.CardletFooterVM;

public class CardletFooterMapper {
    public static void mapToEntity(CardletFooterVM source, CardletFooter destination) {
        if (source == null || destination == null) {
            new IllegalArgumentException();
        }
        destination.setTitle(source.getTitle());
        destination.setFacebookLink(source.getFacebookLink());
        destination.setTwitterLink(source.getTwitterLink());
        destination.setLinkedin_link(source.getLinkedinLink());
    }

    public static CardletFooterVM mapFromEnity(CardletFooter source) {
        if (source == null) {
            return null;
        }
        CardletFooterVM destination = new CardletFooterVM();
        destination.setId(source.getId());
        destination.setTitle(source.getTitle());
        destination.setFacebookLink(source.getFacebookLink());
        destination.setTwitterLink(source.getTwitterLink());
        destination.setLinkedinLink(source.getLinkedin_link());
        return destination;
    }
}
