package itomy.sigterra.service;

import itomy.sigterra.domain.User;
import itomy.sigterra.domain.Visitor;
import itomy.sigterra.repository.VisitorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.LocalDate;

import static itomy.sigterra.domain.enumeration.LocationStatus.NOT_PROCESSES;
import static itomy.sigterra.service.util.RequestUtil.getRequestIp;
import static itomy.sigterra.service.util.RequestUtil.getRequestUserAgent;

/**
 * Service Implementation for managing Visitor.
 */
@Service
@Transactional
public class VisitorService {

    @Inject
    private VisitorRepository visitorRepository;
    @Inject
    private UserService userService;
    @Inject
    private GeoIpService geoIpService;

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
            visitor.setLocationStatus(NOT_PROCESSES);
        }
        visitor = visitorRepository.save(visitor);
        geoIpService.processVisitorLocation(visitor);

        return visitor;
    }

    private Visitor getOrCreateForAnonymous() {
        String ip = getRequestIp();
        String agent = getRequestUserAgent();

        Visitor visitor = visitorRepository.findByIpAndUserAgentAndUserIsNull(ip, agent);
        if (visitor == null) {
            visitor = fromTemplate(ip, agent);
            visitor = visitorRepository.save(visitor);

            geoIpService.processVisitorLocation(visitor);
        }
        return visitor;
    }

    private Visitor fromTemplate(String ip, String agent) {
        Visitor visitor = new Visitor();
        visitor.setIp(ip);
        visitor.setUserAgent(agent);
        visitor.setCreatedDate(LocalDate.now());
        visitor.setLocationStatus(NOT_PROCESSES);

        return visitor;
    }
}
