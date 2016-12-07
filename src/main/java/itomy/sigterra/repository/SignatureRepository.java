package itomy.sigterra.repository;

import itomy.sigterra.domain.Signature;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Signature entity.
 */
@SuppressWarnings("unused")
public interface SignatureRepository extends JpaRepository<Signature,Long> {

}
