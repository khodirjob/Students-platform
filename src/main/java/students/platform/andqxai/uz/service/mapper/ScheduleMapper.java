package students.platform.andqxai.uz.service.mapper;

import org.mapstruct.*;
import students.platform.andqxai.uz.domain.Schedule;
import students.platform.andqxai.uz.service.dto.ScheduleDTO;

/**
 * Mapper for the entity {@link Schedule} and its DTO {@link ScheduleDTO}.
 */
@Mapper(componentModel = "spring")
public interface ScheduleMapper extends EntityMapper<ScheduleDTO, Schedule> {}
