package itomy.sigterra.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A ItemData.
 */
@Entity
@Table(name = "item_data")
public class ItemData implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "modified_date")
    private LocalDate modifiedDate;

    @Column(name = "first_image")
    private String firstImage;

    @Column(name = "second_image")
    private String secondImage;

    @Column(name = "third_image")
    private String thirdImage;

    @Column(name = "link")
    private String link;

    @Column(name = "position")
    private Integer position;

    @Column(name = "tab_index")
    private Integer tabIndex;

    @ManyToOne
    private Item item;

    @OneToOne
    @JoinColumn(unique = true)
    private InputProperties name;

    @OneToOne
    @JoinColumn(unique = true)
    private InputProperties description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public ItemData createdDate(LocalDate createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDate getModifiedDate() {
        return modifiedDate;
    }

    public ItemData modifiedDate(LocalDate modifiedDate) {
        this.modifiedDate = modifiedDate;
        return this;
    }

    public void setModifiedDate(LocalDate modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getFirstImage() {
        return firstImage;
    }

    public ItemData firstImage(String firstImage) {
        this.firstImage = firstImage;
        return this;
    }

    public void setFirstImage(String firstImage) {
        this.firstImage = firstImage;
    }

    public String getSecondImage() {
        return secondImage;
    }

    public ItemData secondImage(String secondImage) {
        this.secondImage = secondImage;
        return this;
    }

    public void setSecondImage(String secondImage) {
        this.secondImage = secondImage;
    }

    public String getThirdImage() {
        return thirdImage;
    }

    public ItemData thirdImage(String thirdImage) {
        this.thirdImage = thirdImage;
        return this;
    }

    public void setThirdImage(String thirdImage) {
        this.thirdImage = thirdImage;
    }

    public String getLink() {
        return link;
    }

    public ItemData link(String link) {
        this.link = link;
        return this;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Integer getPosition() {
        return position;
    }

    public ItemData position(Integer position) {
        this.position = position;
        return this;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getTabIndex() {
        return tabIndex;
    }

    public ItemData tabIndex(Integer tabIndex) {
        this.tabIndex = tabIndex;
        return this;
    }

    public void setTabIndex(Integer tabIndex) {
        this.tabIndex = tabIndex;
    }

    public Item getItem() {
        return item;
    }

    public ItemData item(Item item) {
        this.item = item;
        return this;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public InputProperties getName() {
        return name;
    }

    public ItemData name(InputProperties inputProperties) {
        this.name = inputProperties;
        return this;
    }

    public void setName(InputProperties inputProperties) {
        this.name = inputProperties;
    }

    public InputProperties getDescription() {
        return description;
    }

    public ItemData description(InputProperties inputProperties) {
        this.description = inputProperties;
        return this;
    }

    public void setDescription(InputProperties inputProperties) {
        this.description = inputProperties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ItemData itemData = (ItemData) o;
        if(itemData.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, itemData.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ItemData{" +
            "id=" + id +
            ", createdDate='" + createdDate + "'" +
            ", modifiedDate='" + modifiedDate + "'" +
            ", firstImage='" + firstImage + "'" +
            ", secondImage='" + secondImage + "'" +
            ", thirdImage='" + thirdImage + "'" +
            ", link='" + link + "'" +
            ", position='" + position + "'" +
            ", tabIndex='" + tabIndex + "'" +
            '}';
    }
}
