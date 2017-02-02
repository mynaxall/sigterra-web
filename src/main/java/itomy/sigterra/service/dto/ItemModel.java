package itomy.sigterra.service.dto;

import java.io.Serializable;

/**
 * Created by alexander on 1/31/17.
 */
public class ItemModel implements Serializable {

    private int index;
    private int position;
    private String image1;
    private String image2;
    private String image3;
    private InputModel name;
    private InputModel description;
    private String link;

    public ItemModel() {
    }

    public ItemModel(int index, int position, String image1, String image2, String image3, InputModel name, InputModel description, String link) {
        this.index = index;
        this.position = position;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.name = name;
        this.description = description;
        this.link = link;
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

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
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

    public InputModel getName() {
        return name;
    }

    public void setName(InputModel name) {
        this.name = name;
    }

    public InputModel getDescription() {
        return description;
    }

    public void setDescription(InputModel description) {
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
            "link='" + link + '\'' +
            ", description=" + description +
            ", name=" + name +
            ", image3='" + image3 + '\'' +
            ", image2='" + image2 + '\'' +
            ", image1='" + image1 + '\'' +
            ", position=" + position +
            ", index=" + index +
            '}';
    }
}
