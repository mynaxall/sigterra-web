package itomy.sigterra.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A AddressBook.
 */
@Entity
@Table(name = "address_book")
public class AddressBook implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "modified_date")
    private LocalDate modifiedDate;

    @ManyToOne
    private User user;

    @ManyToOne
    private Cardlet cardlet;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public AddressBook createdDate(LocalDate createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDate getModifiedDate() {
        return modifiedDate;
    }

    public AddressBook modifiedDate(LocalDate modifiedDate) {
        this.modifiedDate = modifiedDate;
        return this;
    }

    public void setModifiedDate(LocalDate modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public User getUser() {
        return user;
    }

    public AddressBook user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Cardlet getCardlet() {
        return cardlet;
    }

    public AddressBook cardlet(Cardlet cardlet) {
        this.cardlet = cardlet;
        return this;
    }

    public void setCardlet(Cardlet cardlet) {
        this.cardlet = cardlet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AddressBook addressBook = (AddressBook) o;
        if(addressBook.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, addressBook.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AddressBook{" +
            "id=" + id +
            ", createdDate='" + createdDate + "'" +
            ", modifiedDate='" + modifiedDate + "'" +
            '}';
    }
}
