package students.platform.andqxai.uz.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import students.platform.andqxai.uz.domain.Cafe;

/**
 * Spring Data JPA repository for the Cafe entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CafeRepository extends JpaRepository<Cafe, Long>, JpaSpecificationExecutor<Cafe> {}
