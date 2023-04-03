package students.platform.andqxai.uz.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link students.platform.andqxai.uz.domain.Vacancy} entity. This class is used
 * in {@link students.platform.andqxai.uz.web.rest.VacancyResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /vacancies?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VacancyCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter companyName;

    private StringFilter jobType;

    private StringFilter description;

    private IntegerFilter vacancyCount;

    private StringFilter location;

    private StringFilter phone;

    private DoubleFilter salary;

    private Boolean distinct;

    public VacancyCriteria() {}

    public VacancyCriteria(VacancyCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.companyName = other.companyName == null ? null : other.companyName.copy();
        this.jobType = other.jobType == null ? null : other.jobType.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.vacancyCount = other.vacancyCount == null ? null : other.vacancyCount.copy();
        this.location = other.location == null ? null : other.location.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
        this.salary = other.salary == null ? null : other.salary.copy();
        this.distinct = other.distinct;
    }

    @Override
    public VacancyCriteria copy() {
        return new VacancyCriteria(this);
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

    public StringFilter getCompanyName() {
        return companyName;
    }

    public StringFilter companyName() {
        if (companyName == null) {
            companyName = new StringFilter();
        }
        return companyName;
    }

    public void setCompanyName(StringFilter companyName) {
        this.companyName = companyName;
    }

    public StringFilter getJobType() {
        return jobType;
    }

    public StringFilter jobType() {
        if (jobType == null) {
            jobType = new StringFilter();
        }
        return jobType;
    }

    public void setJobType(StringFilter jobType) {
        this.jobType = jobType;
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

    public IntegerFilter getVacancyCount() {
        return vacancyCount;
    }

    public IntegerFilter vacancyCount() {
        if (vacancyCount == null) {
            vacancyCount = new IntegerFilter();
        }
        return vacancyCount;
    }

    public void setVacancyCount(IntegerFilter vacancyCount) {
        this.vacancyCount = vacancyCount;
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

    public DoubleFilter getSalary() {
        return salary;
    }

    public DoubleFilter salary() {
        if (salary == null) {
            salary = new DoubleFilter();
        }
        return salary;
    }

    public void setSalary(DoubleFilter salary) {
        this.salary = salary;
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
        final VacancyCriteria that = (VacancyCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(companyName, that.companyName) &&
            Objects.equals(jobType, that.jobType) &&
            Objects.equals(description, that.description) &&
            Objects.equals(vacancyCount, that.vacancyCount) &&
            Objects.equals(location, that.location) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(salary, that.salary) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, companyName, jobType, description, vacancyCount, location, phone, salary, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VacancyCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (companyName != null ? "companyName=" + companyName + ", " : "") +
            (jobType != null ? "jobType=" + jobType + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (vacancyCount != null ? "vacancyCount=" + vacancyCount + ", " : "") +
            (location != null ? "location=" + location + ", " : "") +
            (phone != null ? "phone=" + phone + ", " : "") +
            (salary != null ? "salary=" + salary + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
