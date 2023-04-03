package students.platform.andqxai.uz.domain;

import java.io.Serializable;
import javax.persistence.*;

/**
 * A Apartment.
 */
@Entity
@Table(name = "apartment")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Apartment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "location")
    private String location;

    @Column(name = "phone")
    private String phone;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Double price;

    @Column(name = "requirements")
    private String requirements;

    @Column(name = "room_fit")
    private Integer roomFit;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Apartment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocation() {
        return this.location;
    }

    public Apartment location(String location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhone() {
        return this.phone;
    }

    public Apartment phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescription() {
        return this.description;
    }

    public Apartment description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return this.price;
    }

    public Apartment price(Double price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getRequirements() {
        return this.requirements;
    }

    public Apartment requirements(String requirements) {
        this.setRequirements(requirements);
        return this;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public Integer getRoomFit() {
        return this.roomFit;
    }

    public Apartment roomFit(Integer roomFit) {
        this.setRoomFit(roomFit);
        return this;
    }

    public void setRoomFit(Integer roomFit) {
        this.roomFit = roomFit;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Apartment)) {
            return false;
        }
        return id != null && id.equals(((Apartment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Apartment{" +
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
