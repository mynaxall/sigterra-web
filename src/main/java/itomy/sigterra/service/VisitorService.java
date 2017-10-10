package itomy.sigterra.service;

import itomy.sigterra.domain.User;
import itomy.sigterra.domain.Visitor;
import itomy.sigterra.repository.VisitorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.LocalDate;

import static itomy.sigterra.service.util.RequestUtil.getRequestIp;
import static itomy.sigterra.service.util.RequestUtil.getRequestUserAgent;

/**
 * Service Implementation for managing Visitor.
 */
@Service
@Transactional
public class VisitorService {

    private final Logger log = LoggerFactory.getLogger(VisitorService.class);

    @Inject
    private VisitorRepository visitorRepository;
    @Inject
    private UserService userService;

    public Visitor getOrCreate() {
        User user = userService.getUserWithAuthorities();

        if (user != null) {
            return getByUserOrCreate(user);
        } else {
            return getOrCreateForAnonymous();
        }
    }

    private Visitor getByUserOrCreate(User user) {
        String ip = getRequestIp();
        String agent = getRequestUserAgent();

        Visitor visitor = visitorRepository.findByUserId(user.getId());
        if (visitor == null) {
            visitor = fromTemplate(ip, agent);
            visitor.setUser(user);
        } else {
            visitor.setIp(ip);
            visitor.setUserAgent(agent);
        }
        return visitorRepository.save(visitor);
    }

    private Visitor getOrCreateForAnonymous() {
        String ip = getRequestIp();
        String agent = getRequestUserAgent();

        Visitor visitor = visitorRepository.findByIpAndUserAgentAndUserIsNull(ip, agent);
        if (visitor == null) {
            visitor = fromTemplate(ip, agent);

            visitor = visitorRepository.save(visitor);
        }
        return visitor;
    }

    private Visitor fromTemplate(String ip, String agent) {
        Visitor visitor = new Visitor();
        visitor.setIp(ip);
        visitor.setUserAgent(agent);
        visitor.setCreatedDate(LocalDate.now());

        return visitor;
    }
}
