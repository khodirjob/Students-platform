package students.platform.andqxai.uz.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Authors.
 */
@Entity
@Table(name = "authors")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Authors implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "birth_day")
    private Instant birthDay;

    @Column(name = "ded_day")
    private Instant dedDay;

    @ManyToMany(mappedBy = "authors")
    @JsonIgnoreProperties(value = { "category", "authors" }, allowSetters = true)
    private Set<Book> books = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Authors id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return this.fullName;
    }

    public Authors fullName(String fullName) {
        this.setFullName(fullName);
        return this;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Instant getBirthDay() {
        return this.birthDay;
    }

    public Authors birthDay(Instant birthDay) {
        this.setBirthDay(birthDay);
        return this;
    }

    public void setBirthDay(Instant birthDay) {
        this.birthDay = birthDay;
    }

    public Instant getDedDay() {
        return this.dedDay;
    }

    public Authors dedDay(Instant dedDay) {
        this.setDedDay(dedDay);
        return this;
    }

    public void setDedDay(Instant dedDay) {
        this.dedDay = dedDay;
    }

    public Set<Book> getBooks() {
        return this.books;
    }

    public void setBooks(Set<Book> books) {
        if (this.books != null) {
            this.books.forEach(i -> i.removeAuthors(this));
        }
        if (books != null) {
            books.forEach(i -> i.addAuthors(this));
        }
        this.books = books;
    }

    public Authors books(Set<Book> books) {
        this.setBooks(books);
        return this;
    }

    public Authors addBook(Book book) {
        this.books.add(book);
        book.getAuthors().add(this);
        return this;
    }

    public Authors removeBook(Book book) {
        this.books.remove(book);
        book.getAuthors().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Authors)) {
            return false;
        }
        return id != null && id.equals(((Authors) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Authors{" +
            "id=" + getId() +
            ", fullName='" + getFullName() + "'" +
            ", birthDay='" + getBirthDay() + "'" +
            ", dedDay='" + getDedDay() + "'" +
            "}";
    }
}
