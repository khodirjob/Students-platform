package students.platform.andqxai.uz.service.mapper;

import org.mapstruct.*;
import students.platform.andqxai.uz.domain.Attachment;
import students.platform.andqxai.uz.service.dto.AttachmentDTO;

/**
 * Mapper for the entity {@link Attachment} and its DTO {@link AttachmentDTO}.
 */
@Mapper(componentModel = "spring")
public interface AttachmentMapper extends EntityMapper<AttachmentDTO, Attachment> {}
