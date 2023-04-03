package students.platform.andqxai.uz.service.mapper;

import org.mapstruct.*;
import students.platform.andqxai.uz.domain.Vacancy;
import students.platform.andqxai.uz.service.dto.VacancyDTO;

/**
 * Mapper for the entity {@link Vacancy} and its DTO {@link VacancyDTO}.
 */
@Mapper(componentModel = "spring")
public interface VacancyMapper extends EntityMapper<VacancyDTO, Vacancy> {}
