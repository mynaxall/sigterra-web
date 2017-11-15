package itomy.sigterra.service.resolvers;

import itomy.sigterra.annotation.NoDevelopLogging;
import itomy.sigterra.service.util.DomUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static itomy.sigterra.service.constant.Constants.*;
import static itomy.sigterra.service.util.DomUtils.resolveMetaTag;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
@NoDevelopLogging(reason = "large amount of data")
public class DescriptionResolver {
    private static final List<String> ALLOWED_TAGS = Arrays.asList(
        "a",
        "b",
        "blockquote",
        "i",
        "span",
        "strong",
        "u");

    /**
     * Search description by looking in meta tag &lt;og:description&gt; <br/>
     * or in body tags &lt;h3&gt;, &lt;p&gt; with:
     * <ul>
     * <li>only formatting child tags</li>
     * </ul>
     *
     * @param document page DOM
     * @return resolved description or null
     */
    public String resolve(Document document) {
        String fromMetaTag = resolveMetaTag(document.head(), META_DESCRIPTION_PROPERTY);

        // Don't process body, if found in meta tag
        if (isNotBlank(fromMetaTag)) {
            return fromMetaTag;
        }

        String fromBody = resolveBodyContent(document.body());

        // Not found in meta -> search in body
        if (isNotBlank(fromBody)) {
            return fromBody;
        }

        return null;
    }

    private String resolveBodyContent(Element body) {

        Optional<String> description = Stream.of(H3_TAG, PARAGRAPH_TAG) // search in <h3>, <p> tags
            .map(body::getElementsByTag)            // get all element for tag
            .flatMap(Collection::stream)            // map element's stream of each tag to one stream
            .filter(this::containOnlyAllowedTags)   // get elements only with allowed child tags
            .map(Element::text)                     // get element text
            .filter(StringUtils::isNotBlank)        // text must be present
            .filter(desc -> desc.length() < 150)    // but not logger that 150 symbols
            .findFirst();

        return description.map(DomUtils::resolveBlank).orElse(null);
    }

    private boolean containOnlyAllowedTags(Element element) {
        for (Element e : element.children()) {
            if (isNotAllowed(e)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isNotAllowed(Element e) {
        return !ALLOWED_TAGS.contains(e.tagName());
    }
}
