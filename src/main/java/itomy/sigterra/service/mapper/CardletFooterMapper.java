package itomy.sigterra.service.mapper;

import itomy.sigterra.domain.CardletFooter;
import itomy.sigterra.web.rest.vm.CardletFooterVM;

public class CardletFooterMapper {
    public static void map(CardletFooterVM source, CardletFooter destination) {
        destination.setTitle(source.getTitle());
        destination.setFacebookLink(source.getFacebookLink());
        destination.setTwitterLink(source.getTwitterLink());
        destination.setLinkedin_link(source.getLinkedin_link());
    }

    public static void map(CardletFooter source, CardletFooterVM destination) {
        destination.setId(source.getId());
        destination.setTitle(source.getTitle());
        destination.setFacebookLink(source.getFacebookLink());
        destination.setTwitterLink(source.getTwitterLink());
        destination.setLinkedin_link(source.getLinkedin_link());
    }
}
