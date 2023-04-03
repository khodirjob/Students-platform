package students.platform.andqxai.uz.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import students.platform.andqxai.uz.domain.Vacancy;

/**
 * Spring Data JPA repository for the Vacancy entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VacancyRepository extends JpaRepository<Vacancy, Long>, JpaSpecificationExecutor<Vacancy> {}
