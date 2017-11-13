package itomy.sigterra.service.resolvers;

import itomy.sigterra.annotation.NoDevelopLogging;
import itomy.sigterra.service.util.DomUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static itomy.sigterra.service.constant.Constants.*;
import static itomy.sigterra.service.util.DomUtils.getTextContent;
import static itomy.sigterra.service.util.DomUtils.resolveMetaTag;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
@NoDevelopLogging(reason = "large amount of data")
public class TitleResolver {

    /**
     * Search title by looking in meta tag &lt;og:title&gt;
     * or tags (from great to less priority)
     * <ul>
     * <li>&lt;h1&gt;</li>
     * <li>&lt;h2&gt;</li>
     * <li>&lt;title&gt;</li>
     * </ul>
     *
     * @param document page DOM
     * @return resolved title or null
     */
    public String resolve(Document document) {
        String fromMetaTag = resolveMetaTag(document.head(), META_TITLE_PROPERTY);

        // Don't process body, if found in meta tag
        if (isNotBlank(fromMetaTag)) {
            return fromMetaTag;
        }

        String fromBody = resolveBodyContent(document);

        // Not found in meta -> search in body
        if (isNotBlank(fromBody)) {
            return fromBody;
        }

        // Not found in body -> search in <title>
        String pageTitle = document.title();
        if (isNotBlank(pageTitle)) {
            return pageTitle;
        }

        return null;
    }

    private String resolveBodyContent(Document document) {

        Optional<String> title = Stream.of(H1_TAG, H2_TAG)      // search in <h1>, <h2> tags
            .map(tag -> findTagContent(document, tag))          // get first tag content
            .filter(Objects::nonNull)                           // validate if not null
            .findFirst();

        return title.map(DomUtils::resolveBlank).orElse(null);
    }

    private String findTagContent(Element body, String tagName) {
        return getTextContent(body.getElementsByTag(tagName));
    }
}
