package students.platform.andqxai.uz.service.mapper;

import org.mapstruct.*;
import students.platform.andqxai.uz.domain.Food;
import students.platform.andqxai.uz.service.dto.FoodDTO;

/**
 * Mapper for the entity {@link Food} and its DTO {@link FoodDTO}.
 */
@Mapper(componentModel = "spring")
public interface FoodMapper extends EntityMapper<FoodDTO, Food> {}
