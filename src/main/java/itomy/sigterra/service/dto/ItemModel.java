package itomy.sigterra.service.dto;

import itomy.sigterra.domain.InputProperties;

import java.io.Serializable;

/**
 * Created by alexander on 1/31/17.
 */
public class ItemModel implements Serializable {

    private Long id;
    private int index;
    private int position;
    private String image;
    private String image2;
    private String image3;
    private InputProperties name;
    private InputProperties description;
    private String link;

    public ItemModel() {
    }

    public ItemModel(Long id,int index, int position, String image1, String image2, String image3, InputProperties name, InputProperties description, String link) {
        this.index = index;
        this.position = position;
        this.image = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.name = name;
        this.description = description;
        this.link = link;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image1) {
        this.image = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public InputProperties getName() {
        return name;
    }

    public void setName(InputProperties name) {
        this.name = name;
    }

    public InputProperties getDescription() {
        return description;
    }

    public void setDescription(InputProperties description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return "ItemModel{" +
            "id=" + id +
            ", index=" + index +
            ", position=" + position +
            ", image='" + image + '\'' +
            ", image2='" + image2 + '\'' +
            ", image3='" + image3 + '\'' +
            ", name=" + name +
            ", description=" + description +
            ", link='" + link + '\'' +
            '}';
    }
}
