package students.platform.andqxai.uz.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;

/**
 * A Cafe.
 */
@Entity
@Table(name = "cafe")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Cafe implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "name")
    private String name;

    @Column(name = "location")
    private String location;

    @Column(name = "opening_time")
    private Instant openingTime;

    @Column(name = "close_time")
    private Instant closeTime;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cafe id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public Cafe description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return this.name;
    }

    public Cafe name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return this.location;
    }

    public Cafe location(String location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Instant getOpeningTime() {
        return this.openingTime;
    }

    public Cafe openingTime(Instant openingTime) {
        this.setOpeningTime(openingTime);
        return this;
    }

    public void setOpeningTime(Instant openingTime) {
        this.openingTime = openingTime;
    }

    public Instant getCloseTime() {
        return this.closeTime;
    }

    public Cafe closeTime(Instant closeTime) {
        this.setCloseTime(closeTime);
        return this;
    }

    public void setCloseTime(Instant closeTime) {
        this.closeTime = closeTime;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cafe)) {
            return false;
        }
        return id != null && id.equals(((Cafe) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cafe{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", name='" + getName() + "'" +
            ", location='" + getLocation() + "'" +
            ", openingTime='" + getOpeningTime() + "'" +
            ", closeTime='" + getCloseTime() + "'" +
            "}";
    }
}
