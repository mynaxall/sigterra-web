package itomy.sigterra.service;

import itomy.sigterra.config.JHipsterProperties;
import itomy.sigterra.domain.GeoIpLocation;
import itomy.sigterra.domain.Location;
import itomy.sigterra.domain.Visitor;
import itomy.sigterra.repository.GeoIpLocationRepository;
import itomy.sigterra.repository.VisitorRepository;
import itomy.sigterra.service.util.IPUtils;
import itomy.sigterra.service.util.IpRangeCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import static itomy.sigterra.domain.enumeration.LocationStatus.LOCATED;
import static itomy.sigterra.domain.enumeration.LocationStatus.NOT_LOCATED;

@Service
public class GeoIpService {
    private final Logger log = LoggerFactory.getLogger(GeoIpService.class);

    private GeoIpLocationRepository geoIpLocationRepository;
    private VisitorRepository visitorRepository;

    private final IpRangeCache cache;

    public GeoIpService(GeoIpLocationRepository geoIpLocationRepository, VisitorRepository visitorRepository, JHipsterProperties jHipsterProperties) {
        this.geoIpLocationRepository = geoIpLocationRepository;
        this.visitorRepository = visitorRepository;

        JHipsterProperties.GeoData geoData = jHipsterProperties.getGeoData();
        this.cache = IpRangeCache.newInstance(geoData.getCacheExpirationTimeUnit(), geoData.getCacheExpirationTimeout(), geoData.getCleanCacheTimeUnit(), geoData.getCleanCacheDelay());
    }

    @Async
    public void processVisitorLocation(Visitor visitor) {
        if (log.isDebugEnabled()) {
            log.debug("Async processing of ip = {}, visitor = {}", visitor.getIp(), visitor);
        }
        long decimalIp;
        try {
            decimalIp = IPUtils.ipToDecimal(visitor.getIp());
        } catch (IllegalArgumentException ex) {
            if (log.isWarnEnabled()) {
                log.warn("Get unsupported ip address");
            }
            visitor.setLocationStatus(NOT_LOCATED);
            return;
        }

        Location location = locate(decimalIp);
        if (location == null) {
            visitor.setLocationStatus(NOT_LOCATED);
        } else {
            visitor.setCountry(location.getCountry());
            visitor.setCity(location.getCity());
            visitor.setLocationStatus(LOCATED);
        }
        visitorRepository.save(visitor);
    }

    private Location locate(long decimalIp) {
        Location location = cache.get(decimalIp);
        // In cache
        if (location != null) {
            return location;
        }
        // don't exist in cache, search in DB
        GeoIpLocation geoIpLocation = geoIpLocationRepository.findOneByIp(decimalIp);
        if (geoIpLocation != null) {
            location = toLocation(geoIpLocation);
            cache.put(geoIpLocation.getIpFrom(), location);
            cache.put(geoIpLocation.getIpTo(), null);
            location = cache.get(decimalIp);
        }
        return location;
    }

    private Location toLocation(GeoIpLocation geoIpLocation) {
        return new Location(geoIpLocation.getCountry(), geoIpLocation.getCity());
    }
}
