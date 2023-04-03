package students.platform.andqxai.uz.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link students.platform.andqxai.uz.domain.Schedule} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ScheduleDTO implements Serializable {

    private Long id;

    private String faculty;

    private Integer level;

    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ScheduleDTO)) {
            return false;
        }

        ScheduleDTO scheduleDTO = (ScheduleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, scheduleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ScheduleDTO{" +
            "id=" + getId() +
            ", faculty='" + getFaculty() + "'" +
            ", level=" + getLevel() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
