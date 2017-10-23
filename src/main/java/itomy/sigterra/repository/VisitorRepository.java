package itomy.sigterra.repository;

import itomy.sigterra.domain.Visitor;
import itomy.sigterra.domain.enumeration.LocationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the Visitor entity.
 */
public interface VisitorRepository extends JpaRepository<Visitor, Long> {

    Visitor findByIpAndUserAgentAndUserIsNull(String ip, String agent);

    Visitor findByUserId(Long userId);

    List<Visitor> findAllByLocationStatus(LocationStatus status);
}
