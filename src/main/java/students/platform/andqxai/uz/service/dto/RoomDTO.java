package students.platform.andqxai.uz.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link students.platform.andqxai.uz.domain.Room} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RoomDTO implements Serializable {

    private Long id;

    private String name;

    private String description;

    private String building;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RoomDTO)) {
            return false;
        }

        RoomDTO roomDTO = (RoomDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, roomDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RoomDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", building='" + getBuilding() + "'" +
            "}";
    }
}
