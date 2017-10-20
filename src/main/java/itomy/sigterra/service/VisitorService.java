package itomy.sigterra.service;

import itomy.sigterra.domain.User;
import itomy.sigterra.domain.Visitor;
import itomy.sigterra.repository.VisitorRepository;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.LocalDate;

import static itomy.sigterra.domain.enumeration.LocationStatus.NOT_PROCESSES;

/**
 * Service Implementation for managing Visitor.
 */
@Service
@Transactional
public class VisitorService {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(VisitorService.class);

    @Inject
    private VisitorRepository visitorRepository;
    @Inject
    private GeoIpService geoIpService;

    public Visitor getOrCreate(User user, String ip, String agent) {
        if (user == null) {
            return getOrCreateForAnonymous(ip, agent);
        } else {
            return getByUserOrCreate(user, ip, agent);
        }
    }

    private Visitor getByUserOrCreate(User user, String ip, String agent) {
        Visitor visitor = visitorRepository.findByUserId(user.getId());
        if (visitor == null) {
            visitor = fromTemplate(ip, agent);
            visitor.setUser(user);
        } else {
            visitor.setIp(ip);
            visitor.setUserAgent(agent);
            visitor.setLocationStatus(NOT_PROCESSES);
        }
        visitor = save(visitor);
        geoIpService.processVisitorLocation(visitor);

        return visitor;
    }

    private Visitor getOrCreateForAnonymous(String ip, String agent) {
        Visitor visitor = visitorRepository.findByIpAndUserAgentAndUserIsNull(ip, agent);
        if (visitor == null) {
            visitor = fromTemplate(ip, agent);
            visitor = save(visitor);

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

    public Visitor save(Visitor visitor) {
        return visitorRepository.save(visitor);
    }
}
