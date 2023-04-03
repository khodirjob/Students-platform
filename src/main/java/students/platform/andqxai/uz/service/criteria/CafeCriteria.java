package students.platform.andqxai.uz.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link students.platform.andqxai.uz.domain.Cafe} entity. This class is used
 * in {@link students.platform.andqxai.uz.web.rest.CafeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cafes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CafeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter description;

    private StringFilter name;

    private StringFilter location;

    private InstantFilter openingTime;

    private InstantFilter closeTime;

    private Boolean distinct;

    public CafeCriteria() {}

    public CafeCriteria(CafeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.location = other.location == null ? null : other.location.copy();
        this.openingTime = other.openingTime == null ? null : other.openingTime.copy();
        this.closeTime = other.closeTime == null ? null : other.closeTime.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CafeCriteria copy() {
        return new CafeCriteria(this);
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

    public StringFilter getLocation() {
        return location;
    }

    public StringFilter location() {
        if (location == null) {
            location = new StringFilter();
        }
        return location;
    }

    public void setLocation(StringFilter location) {
        this.location = location;
    }

    public InstantFilter getOpeningTime() {
        return openingTime;
    }

    public InstantFilter openingTime() {
        if (openingTime == null) {
            openingTime = new InstantFilter();
        }
        return openingTime;
    }

    public void setOpeningTime(InstantFilter openingTime) {
        this.openingTime = openingTime;
    }

    public InstantFilter getCloseTime() {
        return closeTime;
    }

    public InstantFilter closeTime() {
        if (closeTime == null) {
            closeTime = new InstantFilter();
        }
        return closeTime;
    }

    public void setCloseTime(InstantFilter closeTime) {
        this.closeTime = closeTime;
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
        final CafeCriteria that = (CafeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(description, that.description) &&
            Objects.equals(name, that.name) &&
            Objects.equals(location, that.location) &&
            Objects.equals(openingTime, that.openingTime) &&
            Objects.equals(closeTime, that.closeTime) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, name, location, openingTime, closeTime, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CafeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (location != null ? "location=" + location + ", " : "") +
            (openingTime != null ? "openingTime=" + openingTime + ", " : "") +
            (closeTime != null ? "closeTime=" + closeTime + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
