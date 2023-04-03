package students.platform.andqxai.uz.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link students.platform.andqxai.uz.domain.Authors} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AuthorsDTO implements Serializable {

    private Long id;

    private String fullName;

    private Instant birthDay;

    private Instant dedDay;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Instant getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Instant birthDay) {
        this.birthDay = birthDay;
    }

    public Instant getDedDay() {
        return dedDay;
    }

    public void setDedDay(Instant dedDay) {
        this.dedDay = dedDay;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AuthorsDTO)) {
            return false;
        }

        AuthorsDTO authorsDTO = (AuthorsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, authorsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AuthorsDTO{" +
            "id=" + getId() +
            ", fullName='" + getFullName() + "'" +
            ", birthDay='" + getBirthDay() + "'" +
            ", dedDay='" + getDedDay() + "'" +
            "}";
    }
}
