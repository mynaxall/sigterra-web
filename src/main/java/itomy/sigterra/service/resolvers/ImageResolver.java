package itomy.sigterra.service.resolvers;

import itomy.sigterra.annotation.NoDevelopLogging;
import itomy.sigterra.service.util.DomUtils;
import itomy.sigterra.service.util.Pair;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static itomy.sigterra.service.constant.Constants.IMAGE_TAG;
import static itomy.sigterra.service.constant.Constants.META_IMAGE_PROPERTY;
import static itomy.sigterra.service.util.DomUtils.resolveImageMetaTag;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
@NoDevelopLogging(reason = "large amount of data")
public class ImageResolver {

    private static final int MIN_WIDTH = 50;
    private static final int MAX_WIDTH = 2000;

    private static final int MIN_HEIGHT = 50;
    private static final int MAX_HEIGHT = 2000;


    /**
     * Search image by looking for image in meta tag &lt;og:image&gt; <br/>
     * or in body tags &lt;img&gt; with:
     * <ul>
     * <li>{@code jpeg} or {@code png} format</li>
     * <li>valid size</li>
     * </ul>
     *
     * @param document page DOM
     * @return resolved image url or null
     */
    public String resolve(Document document) {
        String fromMetaTag = resolveImageMetaTag(document.head(), META_IMAGE_PROPERTY);

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

        List<String> resolvedUrls = body.getElementsByTag(IMAGE_TAG).stream()
            .map(DomUtils::getElementUrl)       // get <img> 'src' value
            .filter(StringUtils::isNotBlank)    // validate
            .map(String::trim)                  // trim
            .collect(toList());                 // collect

        if (resolvedUrls.isEmpty()) {
            return null;
        }

        Optional<String> url = resolvedUrls.stream()
            .map(this::toUrlWithBufferedImagePair)          // map to Pair (Url, BufferedImage)
            .filter(Objects::nonNull)                       // filter null pair
            .filter(pair -> isValidSize(pair.getValue()))   // validate image
            .map(Pair::getKey)                              // get url from Pair
            .findFirst();

        return url.orElse(null);
    }

    private Pair<String, BufferedImage> toUrlWithBufferedImagePair(String url) {

        try (ImageInputStream imageStream =
                 ImageIO.createImageInputStream(new URL(url).openStream())) {

            Iterator<ImageReader> readers = ImageIO.getImageReaders(imageStream);
            ImageReader reader;
            if (!readers.hasNext()) {
                return null;
            } else {
                reader = readers.next();
            }
            String formatName = reader.getFormatName();
            if (isBlank(formatName) || isInvalidFormat(formatName)) {
                imageStream.close();
                return null;
            }
            reader.setInput(imageStream, true, true);
            BufferedImage theImage = reader.read(0);
            reader.dispose();

            return new Pair<>(url, theImage);
        } catch (Exception ex) {
            return null;
        }
    }

    private boolean isInvalidFormat(String formatName) {
        return !formatName.equalsIgnoreCase("jpeg") && !formatName.equalsIgnoreCase("png");
    }

    private boolean isValidSize(BufferedImage image) {
        return image != null
            && isValidWidth(image.getWidth())
            && isValidHeight(image.getHeight());
    }

    private boolean isValidWidth(int width) {
        return width > MIN_WIDTH && width < MAX_WIDTH;
    }

    private boolean isValidHeight(int height) {
        return height > MIN_HEIGHT && height < MAX_HEIGHT;
    }
}
