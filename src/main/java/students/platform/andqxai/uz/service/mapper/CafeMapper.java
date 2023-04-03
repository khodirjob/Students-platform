package students.platform.andqxai.uz.service.mapper;

import org.mapstruct.*;
import students.platform.andqxai.uz.domain.Cafe;
import students.platform.andqxai.uz.service.dto.CafeDTO;

/**
 * Mapper for the entity {@link Cafe} and its DTO {@link CafeDTO}.
 */
@Mapper(componentModel = "spring")
public interface CafeMapper extends EntityMapper<CafeDTO, Cafe> {}
