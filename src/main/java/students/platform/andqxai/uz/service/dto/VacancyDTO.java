package students.platform.andqxai.uz.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link students.platform.andqxai.uz.domain.Vacancy} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VacancyDTO implements Serializable {

    private Long id;

    private String companyName;

    private String jobType;

    private String description;

    private Integer vacancyCount;

    private String location;

    private String phone;

    private Double salary;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getVacancyCount() {
        return vacancyCount;
    }

    public void setVacancyCount(Integer vacancyCount) {
        this.vacancyCount = vacancyCount;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VacancyDTO)) {
            return false;
        }

        VacancyDTO vacancyDTO = (VacancyDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, vacancyDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VacancyDTO{" +
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
