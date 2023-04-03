package students.platform.andqxai.uz.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import students.platform.andqxai.uz.domain.enumeration.AttachmentType;

/**
 * A Attachment.
 */
@Entity
@Table(name = "attachment")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Attachment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "file_original_name")
    private String fileOriginalName;

    @Column(name = "attach_size")
    private Long attachSize;

    @Column(name = "content_type")
    private String contentType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "attachment_type", nullable = true)
    private AttachmentType attachmentType;

    @Column(name = "object_id")
    private Long objectId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Attachment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Attachment name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileOriginalName() {
        return this.fileOriginalName;
    }

    public Attachment fileOriginalName(String fileOriginalName) {
        this.setFileOriginalName(fileOriginalName);
        return this;
    }

    public void setFileOriginalName(String fileOriginalName) {
        this.fileOriginalName = fileOriginalName;
    }

    public Long getAttachSize() {
        return this.attachSize;
    }

    public Attachment attachSize(Long attachSize) {
        this.setAttachSize(attachSize);
        return this;
    }

    public void setAttachSize(Long attachSize) {
        this.attachSize = attachSize;
    }

    public String getContentType() {
        return this.contentType;
    }

    public Attachment contentType(String contentType) {
        this.setContentType(contentType);
        return this;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public AttachmentType getAttachmentType() {
        return this.attachmentType;
    }

    public Attachment attachmentType(AttachmentType attachmentType) {
        this.setAttachmentType(attachmentType);
        return this;
    }

    public void setAttachmentType(AttachmentType attachmentType) {
        this.attachmentType = attachmentType;
    }

    public Long getObjectId() {
        return this.objectId;
    }

    public Attachment objectId(Long objectId) {
        this.setObjectId(objectId);
        return this;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Attachment)) {
            return false;
        }
        return id != null && id.equals(((Attachment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Attachment{" +
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
