package itomy.sigterra.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Cardlet.
 */
@Entity
@Table(name = "cardlet")
public class Cardlet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "modified_date")
    private LocalDate modifiedDate;

    @Column(name = "active")
    private Boolean active;

    @ManyToOne
    private User user;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "cardlet", cascade = CascadeType.ALL)
    private Set<Business> businesses = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "cardlet", cascade = CascadeType.ALL)
    private Set<Item> items = new HashSet<>();

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "cardlet")
    private CardletHeader cardletHeader;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "cardlet")
    private CardletBackground cardletBackground;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "cardlet")
    private CardletLinks cardletLinks;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "cardlet")
    private CardletFooter cardletFooter;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "cardlet", cascade = CascadeType.ALL)
    private Set<CardletTestimonialWidget> cardletTestimonialWidgets = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "cardlet", cascade = CascadeType.ALL)
    private Set<CardletContentLibraryWidget> cardletContentLibraryWidgets = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Cardlet name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public Cardlet createdDate(LocalDate createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDate getModifiedDate() {
        return modifiedDate;
    }

    public Cardlet modifiedDate(LocalDate modifiedDate) {
        this.modifiedDate = modifiedDate;
        return this;
    }

    public void setModifiedDate(LocalDate modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Boolean isActive() {
        return active;
    }

    public Cardlet active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public User getUser() {
        return user;
    }

    public Cardlet user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Business> getBusinesses() {
        return businesses;
    }

    public Cardlet businesses(Set<Business> businesses) {
        this.businesses = businesses;
        return this;
    }

    public Cardlet addBusiness(Business business) {
        businesses.add(business);
        business.setCardlet(this);
        return this;
    }

    public Cardlet removeBusiness(Business business) {
        businesses.remove(business);
        business.setCardlet(null);
        return this;
    }

    public void setBusinesses(Set<Business> businesses) {
        this.businesses = businesses;
    }

    public Set<Item> getItems() {
        return items;
    }

    public Cardlet items(Set<Item> items) {
        this.items = items;
        return this;
    }

    public Cardlet addItem(Item item) {
        items.add(item);
        item.setCardlet(this);
        return this;
    }

    public Cardlet removeItem(Item item) {
        items.remove(item);
        item.setCardlet(null);
        return this;
    }

    public CardletHeader getCardletHeader() {
        return cardletHeader;
    }

    public CardletBackground getCardletBackground() {
        return cardletBackground;
    }

    public CardletLinks getCardletLinks() {
        return cardletLinks;
    }


    public void setItems(Set<Item> items) {
        this.items = items;
    }

    public void setCardletHeader(CardletHeader cardletHeader) {
        this.cardletHeader = cardletHeader;
    }

    public void setCardletBackground(CardletBackground cardletBackground) {
        this.cardletBackground = cardletBackground;
    }

    public void setCardletLinks(CardletLinks cardletLinks) {
        this.cardletLinks = cardletLinks;
    }

    public CardletFooter getCardletFooter() {
        return cardletFooter;
    }

    public void setCardletFooter(CardletFooter cardletFooter) {
        this.cardletFooter = cardletFooter;
    }

    public Set<CardletTestimonialWidget> getCardletTestimonialWidgets() {
        return cardletTestimonialWidgets;
    }

    public void setCardletTestimonialWidgets(Set<CardletTestimonialWidget> cardletTestimonialWidgets) {
        this.cardletTestimonialWidgets = cardletTestimonialWidgets;
    }

    public Set<CardletContentLibraryWidget> getCardletContentLibraryWidgets() {
        return cardletContentLibraryWidgets;
    }

    public void setCardletContentLibraryWidgets(Set<CardletContentLibraryWidget> cardletContentLibraryWidgets) {
        this.cardletContentLibraryWidgets = cardletContentLibraryWidgets;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Cardlet cardlet = (Cardlet) o;
        if (cardlet.id == null || id == null) {
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
