package students.platform.andqxai.uz.service.mapper;

import org.mapstruct.*;
import students.platform.andqxai.uz.domain.Authors;
import students.platform.andqxai.uz.service.dto.AuthorsDTO;

/**
 * Mapper for the entity {@link Authors} and its DTO {@link AuthorsDTO}.
 */
@Mapper(componentModel = "spring")
public interface AuthorsMapper extends EntityMapper<AuthorsDTO, Authors> {}
