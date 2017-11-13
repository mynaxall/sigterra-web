package itomy.sigterra.web.rest;

import itomy.sigterra.annotation.NoDevelopLogging;
import itomy.sigterra.service.resolvers.DescriptionResolver;
import itomy.sigterra.service.resolvers.ImageResolver;
import itomy.sigterra.service.resolvers.TitleResolver;
import itomy.sigterra.web.rest.vm.ResolvedUrlResponseVm;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static itomy.sigterra.service.util.RequestUtil.getRequestUserAgent;

@RestController
@RequestMapping("/api")
@NoDevelopLogging(reason = "large amount of data")
public class ResolveUrlController {
    private static final Logger log = LoggerFactory.getLogger(ResolveUrlController.class);
    private static final String DEFAULT_USER_AGENT =
        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36";

    private final TitleResolver titleResolver;
    private final DescriptionResolver descriptionResolver;
    private final ImageResolver imageResolver;

    public ResolveUrlController(TitleResolver titleResolver,
                                DescriptionResolver descriptionResolver,
                                ImageResolver imageResolver) {

        this.titleResolver = titleResolver;
        this.descriptionResolver = descriptionResolver;
        this.imageResolver = imageResolver;
    }

    @PostMapping("/resolve")
    public ResponseEntity<ResolvedUrlResponseVm> resolve(@RequestParam("url") String url) {
        String userAgent = getUserAgent();
        try {
            Document document = Jsoup.connect(url) // default timeout 3 SECONDS
                .userAgent(userAgent)
                .get();

            ResolvedUrlResponseVm vm = new ResolvedUrlResponseVm();
            vm.setTitle(titleResolver.resolve(document));
            vm.setDescription(descriptionResolver.resolve(document));
            vm.setImageUrl(imageResolver.resolve(document));

            return ResponseEntity.ok(vm);
        } catch (Exception ex) {
            log.error("Error during url resolving = {}", ex.toString());
            return ResponseEntity.ok(new ResolvedUrlResponseVm());
        }

    }

    private String getUserAgent() {
        String userAgent = getRequestUserAgent();
        if (userAgent == null) {
            return DEFAULT_USER_AGENT;
        }
        return userAgent;
    }
}
