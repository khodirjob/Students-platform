package students.platform.andqxai.uz.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;
import students.platform.andqxai.uz.domain.enumeration.AttachmentType;

/**
 * A DTO for the {@link students.platform.andqxai.uz.domain.Attachment} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AttachmentDTO implements Serializable {

    private Long id;

    private String name;

    private String fileOriginalName;

    private Long attachSize;

    private String contentType;

    @NotNull
    private AttachmentType attachmentType;

    private Long objectId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileOriginalName() {
        return fileOriginalName;
    }

    public void setFileOriginalName(String fileOriginalName) {
        this.fileOriginalName = fileOriginalName;
    }

    public Long getAttachSize() {
        return attachSize;
    }

    public void setAttachSize(Long attachSize) {
        this.attachSize = attachSize;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public AttachmentType getAttachmentType() {
        return attachmentType;
    }

    public void setAttachmentType(AttachmentType attachmentType) {
        this.attachmentType = attachmentType;
    }

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AttachmentDTO)) {
            return false;
        }

        AttachmentDTO attachmentDTO = (AttachmentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, attachmentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AttachmentDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", fileOriginalName='" + getFileOriginalName() + "'" +
            ", attachSize=" + getAttachSize() +
            ", contentType='" + getContentType() + "'" +
            ", attachmentType='" + getAttachmentType() + "'" +
            ", objectId=" + getObjectId() +
            "}";
    }
}
