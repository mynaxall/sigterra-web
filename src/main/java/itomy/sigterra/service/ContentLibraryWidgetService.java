package itomy.sigterra.service;

import itomy.sigterra.domain.*;
import itomy.sigterra.repository.*;
import itomy.sigterra.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

import static itomy.sigterra.service.util.RequestUtil.getRequestIp;
import static itomy.sigterra.service.util.RequestUtil.getRequestUserAgent;

@Service
@Transactional
public class ContentLibraryWidgetService {

    private final Logger log = LoggerFactory.getLogger(ContentLibraryWidgetService.class);

    @Inject
    private ContentLibraryWidgetLikesRepository widgetLikesRepository;

    @Inject
    private CardletContentLibraryWidgetRepository libraryWidgetRepository;

    @Inject
    private VisitorRepository visitorRepository;
    @Inject
    private VisitorService visitorService;

    @Inject
    private UserService userService;

    @Inject
    private CardletRepository cardletRepository;

    @Inject
    private ContentLibraryWidgetViewsRepository widgetViewsRepository;

    public void likeCardletContentLibraryWidget(Cardlet cardlet, CardletContentLibraryWidget widget) {
        User currentUser = userService.getUserWithAuthorities();
        Visitor visitor = visitorService.getOrCreate(currentUser, getRequestIp(), getRequestUserAgent());
        ContentLibraryWidgetLikes storeLike = widgetLikesRepository.findByLike(cardlet, widget, visitor);
        if (storeLike == null) {
            ContentLibraryWidgetLikes like = new ContentLibraryWidgetLikes(new Timestamp(new Date().getTime()), visitor, cardlet, widget);
            widgetLikesRepository.save(like);
        } else {
            throw new BadRequestAlertException("content_library_widget_likes",
                "This content library widget has been liked");
        }
    }


    public void viewCardletContentLibraryWidget(Cardlet cardlet, CardletContentLibraryWidget widget) {
        User currentUser = userService.getUserWithAuthorities();
        Visitor visitor = visitorService.getOrCreate(currentUser, getRequestIp(), getRequestUserAgent());

        if (Objects.equals(currentUser, cardlet.getUser())) {
            log.error("Current user view this page");
            return;
        }

        ContentLibraryWidgetViews views = widgetViewsRepository.findByViews(cardlet, widget, visitor);
        if (views == null) {
            ContentLibraryWidgetViews newViews = new ContentLibraryWidgetViews(new Timestamp(new Date().getTime()), visitor, cardlet, widget);
            widgetViewsRepository.save(newViews);
        } else {
            log.error("Current user has been view this page");
            return;
        }
    }
}
