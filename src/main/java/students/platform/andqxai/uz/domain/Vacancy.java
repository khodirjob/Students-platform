package students.platform.andqxai.uz.domain;

import java.io.Serializable;
import javax.persistence.*;

/**
 * A Vacancy.
 */
@Entity
@Table(name = "vacancy")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Vacancy implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "job_type")
    private String jobType;

    @Column(name = "description")
    private String description;

    @Column(name = "vacancy_count")
    private Integer vacancyCount;

    @Column(name = "location")
    private String location;

    @Column(name = "phone")
    private String phone;

    @Column(name = "salary")
    private Double salary;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Vacancy id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public Vacancy companyName(String companyName) {
        this.setCompanyName(companyName);
        return this;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJobType() {
        return this.jobType;
    }

    public Vacancy jobType(String jobType) {
        this.setJobType(jobType);
        return this;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getDescription() {
        return this.description;
    }

    public Vacancy description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getVacancyCount() {
        return this.vacancyCount;
    }

    public Vacancy vacancyCount(Integer vacancyCount) {
        this.setVacancyCount(vacancyCount);
        return this;
    }

    public void setVacancyCount(Integer vacancyCount) {
        this.vacancyCount = vacancyCount;
    }

    public String getLocation() {
        return this.location;
    }

    public Vacancy location(String location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhone() {
        return this.phone;
    }

    public Vacancy phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Double getSalary() {
        return this.salary;
    }

    public Vacancy salary(Double salary) {
        this.setSalary(salary);
        return this;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vacancy)) {
            return false;
        }
        return id != null && id.equals(((Vacancy) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Vacancy{" +
            "id=" + getId() +
            ", companyName='" + getCompanyName() + "'" +
            ", jobType='" + getJobType() + "'" +
            ", description='" + getDescription() + "'" +
            ", vacancyCount=" + getVacancyCount() +
            ", location='" + getLocation() + "'" +
            ", phone='" + getPhone() + "'" +
            ", salary=" + getSalary() +
            "}";
    }
}
