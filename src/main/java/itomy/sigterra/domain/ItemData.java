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

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

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

    @ManyToOne
    private Item item;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public ItemData title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public ItemData description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
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
            ", title='" + title + "'" +
            ", description='" + description + "'" +
            ", createdDate='" + createdDate + "'" +
            ", modifiedDate='" + modifiedDate + "'" +
            ", firstImage='" + firstImage + "'" +
            ", secondImage='" + secondImage + "'" +
            ", thirdImage='" + thirdImage + "'" +
            '}';
    }
}
