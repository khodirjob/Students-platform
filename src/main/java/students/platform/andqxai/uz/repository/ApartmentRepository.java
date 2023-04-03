package students.platform.andqxai.uz.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import students.platform.andqxai.uz.domain.Apartment;

/**
 * Spring Data JPA repository for the Apartment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, Long>, JpaSpecificationExecutor<Apartment> {}
