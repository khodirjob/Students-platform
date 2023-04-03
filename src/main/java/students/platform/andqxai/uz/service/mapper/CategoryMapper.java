package students.platform.andqxai.uz.service.mapper;

import org.mapstruct.*;
import students.platform.andqxai.uz.domain.Category;
import students.platform.andqxai.uz.service.dto.CategoryDTO;

/**
 * Mapper for the entity {@link Category} and its DTO {@link CategoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface CategoryMapper extends EntityMapper<CategoryDTO, Category> {}
