package itomy.sigterra.service.mapper;

import itomy.sigterra.domain.CardletLinks;
import itomy.sigterra.web.rest.vm.CardletLinksVM;

public class CardletLinksMapper {
    public static void map(CardletLinksVM source, CardletLinks destination) {
        destination.setTitle(source.getTitle());
        destination.setLogo1(source.getLogoUrl1());
        destination.setName1(source.getName1());
        destination.setUrl2(source.getUrl2());
        destination.setLogo2(source.getLogoUrl2());
        destination.setName2(source.getName2());
        destination.setUrl2(source.getUrl2());
        destination.setLogo3(source.getLogoUrl3());
        destination.setName3(source.getName3());
        destination.setUrl3(source.getUrl3());
    }

    public static void map(CardletLinks source, CardletLinksVM destination) {
        destination.setId(source.getId());
        destination.setTitle(source.getTitle());
        destination.setLogoUrl1(source.getLogo1());
        destination.setName1(source.getName1());
        destination.setUrl2(source.getUrl2());
        destination.setLogoUrl2(source.getLogo2());
        destination.setName2(source.getName2());
        destination.setUrl2(source.getUrl2());
        destination.setLogoUrl3(source.getLogo3());
        destination.setName3(source.getName3());
        destination.setUrl3(source.getUrl3());
    }
}
