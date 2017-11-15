package itomy.sigterra.service.util;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static itomy.sigterra.service.constant.Constants.META_CONTENT_ATTR;
import static itomy.sigterra.service.constant.Constants.SRC_ATTR;
import static org.apache.commons.lang3.StringUtils.isBlank;

public final class DomUtils {

    private DomUtils() {
    }

    public static String resolveBlank(String toResolve) {
        return isBlank(toResolve) ? null : toResolve;
    }

    public static String resolveMetaTag(Element head, String tag) {
        List<Element> imageList = head.select(tag);

        return getValue(imageList);
    }

    public static String resolveImageMetaTag(Element head, String tag) {
        List<Element> imageList = head.select(tag);

        return getImageValue(imageList);
    }

    private static String getValue(List<Element> list) {
        if (list.isEmpty()) {
            return null;
        }
        String value = list.get(0).attr(META_CONTENT_ATTR);
        return isBlank(value) ? null : value;
    }

    private static String getImageValue(List<Element> list) {
        if (list.isEmpty()) {
            return null;
        }
        String value = list.get(0).absUrl(META_CONTENT_ATTR);
        return isBlank(value) ? null : value;
    }

    public static String getElementUrl(Element image) {
        return image.absUrl(SRC_ATTR);
    }

    public static String getTextContent(List<Element> list) {

        Optional<String> result = list.stream()
            .filter(Objects::nonNull)               // filter null
            .map(Element::text)                     // get text value from element
            .filter(StringUtils::isNotBlank)        // must not be blank
            .map(String::trim)                      // trim
            .findFirst();

        return result.map(DomUtils::resolveBlank).orElse(null);
    }
}
