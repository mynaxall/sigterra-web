package itomy.sigterra.repository;

import itomy.sigterra.domain.GeoIpLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data JPA repository for the GeoIpLocation entity.
 */
public interface GeoIpLocationRepository extends JpaRepository<GeoIpLocation, Long> {

    @Query("select range from GeoIpLocation range where :ip between range.ipFrom AND range.ipTo")
    GeoIpLocation findOneByIp(@Param("ip") long ip);
}
