package students.platform.andqxai.uz.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link students.platform.andqxai.uz.domain.Cafe} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CafeDTO implements Serializable {

    private Long id;

    private String description;

    private String name;

    private String location;

    private Instant openingTime;

    private Instant closeTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Instant getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(Instant openingTime) {
        this.openingTime = openingTime;
    }

    public Instant getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Instant closeTime) {
        this.closeTime = closeTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CafeDTO)) {
            return false;
        }

        CafeDTO cafeDTO = (CafeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cafeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CafeDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", name='" + getName() + "'" +
            ", location='" + getLocation() + "'" +
            ", openingTime='" + getOpeningTime() + "'" +
            ", closeTime='" + getCloseTime() + "'" +
            "}";
    }
}
