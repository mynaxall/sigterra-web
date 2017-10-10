package itomy.sigterra.repository;

import itomy.sigterra.domain.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Visitor entity.
 */
public interface VisitorRepository extends JpaRepository<Visitor, Long> {

    Visitor findByIpAndUserAgentAndUserIsNull(String ip, String agent);

    Visitor findByUserId(Long userId);
}
