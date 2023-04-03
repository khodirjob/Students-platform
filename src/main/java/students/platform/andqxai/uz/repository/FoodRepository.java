package students.platform.andqxai.uz.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import students.platform.andqxai.uz.domain.Food;

/**
 * Spring Data JPA repository for the Food entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FoodRepository extends JpaRepository<Food, Long>, JpaSpecificationExecutor<Food> {}
