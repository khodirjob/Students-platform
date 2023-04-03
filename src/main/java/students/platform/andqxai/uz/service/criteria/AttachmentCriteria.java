package students.platform.andqxai.uz.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import students.platform.andqxai.uz.domain.enumeration.AttachmentType;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link students.platform.andqxai.uz.domain.Attachment} entity. This class is used
 * in {@link students.platform.andqxai.uz.web.rest.AttachmentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /attachments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AttachmentCriteria implements Serializable, Criteria {

    /**
     * Class for filtering AttachmentType
     */
    public static class AttachmentTypeFilter extends Filter<AttachmentType> {

        public AttachmentTypeFilter() {}

        public AttachmentTypeFilter(AttachmentTypeFilter filter) {
            super(filter);
        }

        @Override
        public AttachmentTypeFilter copy() {
            return new AttachmentTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter fileOriginalName;

    private LongFilter attachSize;

    private StringFilter contentType;

    private AttachmentTypeFilter attachmentType;

    private LongFilter objectId;

    private Boolean distinct;

    public AttachmentCriteria() {}

    public AttachmentCriteria(AttachmentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.fileOriginalName = other.fileOriginalName == null ? null : other.fileOriginalName.copy();
        this.attachSize = other.attachSize == null ? null : other.attachSize.copy();
        this.contentType = other.contentType == null ? null : other.contentType.copy();
        this.attachmentType = other.attachmentType == null ? null : other.attachmentType.copy();
        this.objectId = other.objectId == null ? null : other.objectId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AttachmentCriteria copy() {
        return new AttachmentCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getFileOriginalName() {
        return fileOriginalName;
    }

    public StringFilter fileOriginalName() {
        if (fileOriginalName == null) {
            fileOriginalName = new StringFilter();
        }
        return fileOriginalName;
    }

    public void setFileOriginalName(StringFilter fileOriginalName) {
        this.fileOriginalName = fileOriginalName;
    }

    public LongFilter getAttachSize() {
        return attachSize;
    }

    public LongFilter attachSize() {
        if (attachSize == null) {
            attachSize = new LongFilter();
        }
        return attachSize;
    }

    public void setAttachSize(LongFilter attachSize) {
        this.attachSize = attachSize;
    }

    public StringFilter getContentType() {
        return contentType;
    }

    public StringFilter contentType() {
        if (contentType == null) {
            contentType = new StringFilter();
        }
        return contentType;
    }

    public void setContentType(StringFilter contentType) {
        this.contentType = contentType;
    }

    public AttachmentTypeFilter getAttachmentType() {
        return attachmentType;
    }

    public AttachmentTypeFilter attachmentType() {
        if (attachmentType == null) {
            attachmentType = new AttachmentTypeFilter();
        }
        return attachmentType;
    }

    public void setAttachmentType(AttachmentTypeFilter attachmentType) {
        this.attachmentType = attachmentType;
    }

    public LongFilter getObjectId() {
        return objectId;
    }

    public LongFilter objectId() {
        if (objectId == null) {
            objectId = new LongFilter();
        }
        return objectId;
    }

    public void setObjectId(LongFilter objectId) {
        this.objectId = objectId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AttachmentCriteria that = (AttachmentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(fileOriginalName, that.fileOriginalName) &&
            Objects.equals(attachSize, that.attachSize) &&
            Objects.equals(contentType, that.contentType) &&
            Objects.equals(attachmentType, that.attachmentType) &&
            Objects.equals(objectId, that.objectId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, fileOriginalName, attachSize, contentType, attachmentType, objectId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AttachmentCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (fileOriginalName != null ? "fileOriginalName=" + fileOriginalName + ", " : "") +
            (attachSize != null ? "attachSize=" + attachSize + ", " : "") +
            (contentType != null ? "contentType=" + contentType + ", " : "") +
            (attachmentType != null ? "attachmentType=" + attachmentType + ", " : "") +
            (objectId != null ? "objectId=" + objectId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
