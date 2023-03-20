package students.platform.andqxai.uz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import students.platform.andqxai.uz.domain.Authority;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
