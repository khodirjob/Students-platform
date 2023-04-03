package students.platform.andqxai.uz.service.mapper;

import org.mapstruct.*;
import students.platform.andqxai.uz.domain.Apartment;
import students.platform.andqxai.uz.service.dto.ApartmentDTO;

/**
 * Mapper for the entity {@link Apartment} and its DTO {@link ApartmentDTO}.
 */
@Mapper(componentModel = "spring")
public interface ApartmentMapper extends EntityMapper<ApartmentDTO, Apartment> {}
