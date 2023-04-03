package students.platform.andqxai.uz.domain;

import java.io.Serializable;
import javax.persistence.*;

/**
 * A Schedule.
 */
@Entity
@Table(name = "schedule")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Schedule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "faculty")
    private String faculty;

    @Column(name = "level")
    private Integer level;

    @Column(name = "description")
    private String description;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Schedule id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFaculty() {
        return this.faculty;
    }

    public Schedule faculty(String faculty) {
        this.setFaculty(faculty);
        return this;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public Integer getLevel() {
        return this.level;
    }

    public Schedule level(Integer level) {
        this.setLevel(level);
        return this;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getDescription() {
        return this.description;
    }

    public Schedule description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Schedule)) {
            return false;
        }
        return id != null && id.equals(((Schedule) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Schedule{" +
            "id=" + getId() +
            ", faculty='" + getFaculty() + "'" +
            ", level=" + getLevel() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
