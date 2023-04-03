package students.platform.andqxai.uz.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link students.platform.andqxai.uz.domain.Apartment} entity. This class is used
 * in {@link students.platform.andqxai.uz.web.rest.ApartmentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /apartments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ApartmentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter location;

    private StringFilter phone;

    private StringFilter description;

    private DoubleFilter price;

    private StringFilter requirements;

    private IntegerFilter roomFit;

    private Boolean distinct;

    public ApartmentCriteria() {}

    public ApartmentCriteria(ApartmentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.location = other.location == null ? null : other.location.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.price = other.price == null ? null : other.price.copy();
        this.requirements = other.requirements == null ? null : other.requirements.copy();
        this.roomFit = other.roomFit == null ? null : other.roomFit.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ApartmentCriteria copy() {
        return new ApartmentCriteria(this);
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

    public StringFilter getPhone() {
        return phone;
    }

    public StringFilter phone() {
        if (phone == null) {
            phone = new StringFilter();
        }
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
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

    public DoubleFilter getPrice() {
        return price;
    }

    public DoubleFilter price() {
        if (price == null) {
            price = new DoubleFilter();
        }
        return price;
    }

    public void setPrice(DoubleFilter price) {
        this.price = price;
    }

    public StringFilter getRequirements() {
        return requirements;
    }

    public StringFilter requirements() {
        if (requirements == null) {
            requirements = new StringFilter();
        }
        return requirements;
    }

    public void setRequirements(StringFilter requirements) {
        this.requirements = requirements;
    }

    public IntegerFilter getRoomFit() {
        return roomFit;
    }

    public IntegerFilter roomFit() {
        if (roomFit == null) {
            roomFit = new IntegerFilter();
        }
        return roomFit;
    }

    public void setRoomFit(IntegerFilter roomFit) {
        this.roomFit = roomFit;
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
        final ApartmentCriteria that = (ApartmentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(location, that.location) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(description, that.description) &&
            Objects.equals(price, that.price) &&
            Objects.equals(requirements, that.requirements) &&
            Objects.equals(roomFit, that.roomFit) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, location, phone, description, price, requirements, roomFit, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApartmentCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (location != null ? "location=" + location + ", " : "") +
            (phone != null ? "phone=" + phone + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (price != null ? "price=" + price + ", " : "") +
            (requirements != null ? "requirements=" + requirements + ", " : "") +
            (roomFit != null ? "roomFit=" + roomFit + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
