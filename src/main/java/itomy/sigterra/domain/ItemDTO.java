package itomy.sigterra.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

/**
 * A Item.
 */

public class ItemDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private String icon;

    private LocalDate createdDate;

    private LocalDate modifiDate;

    private String mainColor;

    private String color;

    @ManyToOne
    private TabType tabType;

    private Set<ItemDataDTO> itemData;

    public ItemDTO(Long id, String name, String icon, LocalDate createdDate, LocalDate modifiDate, String mainColor, String color, TabType tabType, Set<ItemDataDTO> itemData) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.createdDate = createdDate;
        this.modifiDate = modifiDate;
        this.mainColor = mainColor;
        this.color = color;
        this.tabType = tabType;
        this.itemData = itemData;
    }

    public ItemDTO(Long id, String name, String icon, LocalDate createdDate, LocalDate modifiDate, String mainColor, String color, TabType tabType) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.createdDate = createdDate;
        this.modifiDate = modifiDate;
        this.mainColor = mainColor;
        this.color = color;
        this.tabType = tabType;
    }

    public ItemDTO(Item item) {
        this(item.getId(), item.getName(), item.getIcon() ,item.getCreatedDate(), item.getModifiDate(), item.getMainColor(), item.getColor(), item.getTabType() );
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDate getModifiDate() {
        return modifiDate;
    }

    public void setModifiDate(LocalDate modifiDate) {
        this.modifiDate = modifiDate;
    }

    public String getMainColor() {
        return mainColor;
    }

    public void setMainColor(String mainColor) {
        this.mainColor = mainColor;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public TabType getTabType() {
        return tabType;
    }

    public void setTabType(TabType tabType) {
        this.tabType = tabType;
    }

    public Set<ItemDataDTO> getItemData() {
        return itemData;
    }

    public void setItemData(Set<ItemDataDTO> itemData) {
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
        ItemDTO item = (ItemDTO) o;
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
            '}';
    }
}
