package students.platform.andqxai.uz.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link students.platform.andqxai.uz.domain.Authors} entity. This class is used
 * in {@link students.platform.andqxai.uz.web.rest.AuthorsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /authors?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AuthorsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter fullName;

    private InstantFilter birthDay;

    private InstantFilter dedDay;

    private LongFilter bookId;

    private Boolean distinct;

    public AuthorsCriteria() {}

    public AuthorsCriteria(AuthorsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.fullName = other.fullName == null ? null : other.fullName.copy();
        this.birthDay = other.birthDay == null ? null : other.birthDay.copy();
        this.dedDay = other.dedDay == null ? null : other.dedDay.copy();
        this.bookId = other.bookId == null ? null : other.bookId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AuthorsCriteria copy() {
        return new AuthorsCriteria(this);
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

    public StringFilter getFullName() {
        return fullName;
    }

    public StringFilter fullName() {
        if (fullName == null) {
            fullName = new StringFilter();
        }
        return fullName;
    }

    public void setFullName(StringFilter fullName) {
        this.fullName = fullName;
    }

    public InstantFilter getBirthDay() {
        return birthDay;
    }

    public InstantFilter birthDay() {
        if (birthDay == null) {
            birthDay = new InstantFilter();
        }
        return birthDay;
    }

    public void setBirthDay(InstantFilter birthDay) {
        this.birthDay = birthDay;
    }

    public InstantFilter getDedDay() {
        return dedDay;
    }

    public InstantFilter dedDay() {
        if (dedDay == null) {
            dedDay = new InstantFilter();
        }
        return dedDay;
    }

    public void setDedDay(InstantFilter dedDay) {
        this.dedDay = dedDay;
    }

    public LongFilter getBookId() {
        return bookId;
    }

    public LongFilter bookId() {
        if (bookId == null) {
            bookId = new LongFilter();
        }
        return bookId;
    }

    public void setBookId(LongFilter bookId) {
        this.bookId = bookId;
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
        final AuthorsCriteria that = (AuthorsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fullName, that.fullName) &&
            Objects.equals(birthDay, that.birthDay) &&
            Objects.equals(dedDay, that.dedDay) &&
            Objects.equals(bookId, that.bookId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fullName, birthDay, dedDay, bookId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AuthorsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (fullName != null ? "fullName=" + fullName + ", " : "") +
            (birthDay != null ? "birthDay=" + birthDay + ", " : "") +
            (dedDay != null ? "dedDay=" + dedDay + ", " : "") +
            (bookId != null ? "bookId=" + bookId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
