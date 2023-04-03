package students.platform.andqxai.uz.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link students.platform.andqxai.uz.domain.Schedule} entity. This class is used
 * in {@link students.platform.andqxai.uz.web.rest.ScheduleResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /schedules?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ScheduleCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter faculty;

    private IntegerFilter level;

    private StringFilter description;

    private Boolean distinct;

    public ScheduleCriteria() {}

    public ScheduleCriteria(ScheduleCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.faculty = other.faculty == null ? null : other.faculty.copy();
        this.level = other.level == null ? null : other.level.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ScheduleCriteria copy() {
        return new ScheduleCriteria(this);
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

    public StringFilter getFaculty() {
        return faculty;
    }

    public StringFilter faculty() {
        if (faculty == null) {
            faculty = new StringFilter();
        }
        return faculty;
    }

    public void setFaculty(StringFilter faculty) {
        this.faculty = faculty;
    }

    public IntegerFilter getLevel() {
        return level;
    }

    public IntegerFilter level() {
        if (level == null) {
            level = new IntegerFilter();
        }
        return level;
    }

    public void setLevel(IntegerFilter level) {
        this.level = level;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
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
        final ScheduleCriteria that = (ScheduleCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(faculty, that.faculty) &&
            Objects.equals(level, that.level) &&
            Objects.equals(description, that.description) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, faculty, level, description, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ScheduleCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (faculty != null ? "faculty=" + faculty + ", " : "") +
            (level != null ? "level=" + level + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
