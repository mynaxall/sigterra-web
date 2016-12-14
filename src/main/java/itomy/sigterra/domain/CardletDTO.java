package itomy.sigterra.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Cardlet.
 */

public class CardletDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    private Long id;


    private String name;


    private LocalDate createdDate;

    private LocalDate modifiedDate;


    private Boolean active;


    private User user;


    private Set<BusinessDTO> businesses = new HashSet<>();

    private Set<ItemDTO> items = new HashSet<>();

    public CardletDTO() {
    }

    public CardletDTO(Long id, String name, LocalDate createdDate, LocalDate modifiedDate, Boolean active, User user, Set<BusinessDTO> businesses, Set<ItemDTO> items) {
        this.id = id;
        this.name = name;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.active = active;
        this.user = user;
        this.businesses = businesses;
        this.items = items;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

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

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDate getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(LocalDate modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<BusinessDTO> getBusinesses() {
        return businesses;
    }

    public void setBusinesses(Set<BusinessDTO> businesses) {
        this.businesses = businesses;
    }

    public Set<ItemDTO> getItems() {
        return items;
    }

    public void setItems(Set<ItemDTO> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CardletDTO cardlet = (CardletDTO) o;
        if(cardlet.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, cardlet.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Cardlet{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", createdDate='" + createdDate + "'" +
            ", modifiedDate='" + modifiedDate + "'" +
            ", active='" + active + "'" +
            '}';
    }
}
