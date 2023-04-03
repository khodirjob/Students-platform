package students.platform.andqxai.uz.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import students.platform.andqxai.uz.domain.Authors;

/**
 * Spring Data JPA repository for the Authors entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AuthorsRepository extends JpaRepository<Authors, Long>, JpaSpecificationExecutor<Authors> {}
