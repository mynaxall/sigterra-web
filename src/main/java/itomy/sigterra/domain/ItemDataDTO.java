package itomy.sigterra.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A ItemData.
 */

public class ItemDataDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    private Long id;

    private String title;

    private String description;

    private LocalDate createdDate;

    private LocalDate modifiedDate;

    private String firstImage;

    private String secondImage;

    private String thirdImage;

    public ItemDataDTO(Long id, String title, String description, LocalDate createdDate, LocalDate modifiedDate, String firstImage, String secondImage, String thirdImage) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.firstImage = firstImage;
        this.secondImage = secondImage;
        this.thirdImage = thirdImage;
    }

    public ItemDataDTO(ItemData item) {

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getFirstImage() {
        return firstImage;
    }

    public void setFirstImage(String firstImage) {
        this.firstImage = firstImage;
    }

    public String getSecondImage() {
        return secondImage;
    }

    public void setSecondImage(String secondImage) {
        this.secondImage = secondImage;
    }

    public String getThirdImage() {
        return thirdImage;
    }

    public void setThirdImage(String thirdImage) {
        this.thirdImage = thirdImage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ItemDataDTO itemData = (ItemDataDTO) o;
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
