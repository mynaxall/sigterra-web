package itomy.sigterra.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Item.
 */
@Entity
@Table(name = "item")
public class Item implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "icon")
    private String icon;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "modifi_date")
    private LocalDate modifiDate;

    @Column(name = "main_color")
    private String mainColor;

    @Column(name = "color")
    private String color;

    @Column(name = "pisition")
    private Integer pisition;

    @ManyToOne
    private Cardlet cardlet;

    @ManyToOne
    private TabType tabType;

    @OneToMany(mappedBy = "item")
    @JsonIgnore
    private Set<ItemData> itemData = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Item name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public Item icon(String icon) {
        this.icon = icon;
        return this;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public Item createdDate(LocalDate createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDate getModifiDate() {
        return modifiDate;
    }

    public Item modifiDate(LocalDate modifiDate) {
        this.modifiDate = modifiDate;
        return this;
    }

    public void setModifiDate(LocalDate modifiDate) {
        this.modifiDate = modifiDate;
    }

    public String getMainColor() {
        return mainColor;
    }

    public Item mainColor(String mainColor) {
        this.mainColor = mainColor;
        return this;
    }

    public void setMainColor(String mainColor) {
        this.mainColor = mainColor;
    }

    public String getColor() {
        return color;
    }

    public Item color(String color) {
        this.color = color;
        return this;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getPisition() {
        return pisition;
    }

    public Item pisition(Integer pisition) {
        this.pisition = pisition;
        return this;
    }

    public void setPisition(Integer pisition) {
        this.pisition = pisition;
    }

    public Cardlet getCardlet() {
        return cardlet;
    }

    public Item cardlet(Cardlet cardlet) {
        this.cardlet = cardlet;
        return this;
    }

    public void setCardlet(Cardlet cardlet) {
        this.cardlet = cardlet;
    }

    public TabType getTabType() {
        return tabType;
    }

    public Item tabType(TabType tabType) {
        this.tabType = tabType;
        return this;
    }

    public void setTabType(TabType tabType) {
        this.tabType = tabType;
    }

    public Set<ItemData> getItemData() {
        return itemData;
    }

    public Item itemData(Set<ItemData> itemData) {
        this.itemData = itemData;
        return this;
    }


    public void setItemData(Set<ItemData> itemData) {
        this.itemData = itemData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Item item = (Item) o;
        if(item.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, item.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Item{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", icon='" + icon + "'" +
            ", createdDate='" + createdDate + "'" +
            ", modifiDate='" + modifiDate + "'" +
            ", mainColor='" + mainColor + "'" +
            ", color='" + color + "'" +
            ", pisition='" + pisition + "'" +
            '}';
    }
}
