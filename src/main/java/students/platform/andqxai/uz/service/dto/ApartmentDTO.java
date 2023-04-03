package students.platform.andqxai.uz.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link students.platform.andqxai.uz.domain.Apartment} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ApartmentDTO implements Serializable {

    private Long id;

    private String location;

    private String phone;

    private String description;

    private Double price;

    private String requirements;

    private Integer roomFit;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public Integer getRoomFit() {
        return roomFit;
    }

    public void setRoomFit(Integer roomFit) {
        this.roomFit = roomFit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApartmentDTO)) {
            return false;
        }

        ApartmentDTO apartmentDTO = (ApartmentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, apartmentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApartmentDTO{" +
            "id=" + getId() +
            ", location='" + getLocation() + "'" +
            ", phone='" + getPhone() + "'" +
            ", description='" + getDescription() + "'" +
            ", price=" + getPrice() +
            ", requirements='" + getRequirements() + "'" +
            ", roomFit=" + getRoomFit() +
            "}";
    }
}
