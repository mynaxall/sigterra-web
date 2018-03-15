package itomy.sigterra.web.rest.vm;

import itomy.sigterra.domain.CardletBackground;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Size;

@Validated
public class CardletBackgroundVM {
    private Long id;
    @Size(max = 255)
    private String imageUrl;
    private boolean textColor;
    @Size(max = 140)
    private String text;

    public CardletBackgroundVM() {
    }

    public CardletBackgroundVM(Long id, String imageUrl, boolean textColor, String text) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.textColor = textColor;
        this.text = text;
    }

    public CardletBackgroundVM(CardletBackground cardletBackground){
        mapFromCardletBackground(cardletBackground);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isTextColor() {
        return textColor;
    }

    public void setTextColor(boolean textColor) {
        this.textColor = textColor;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "CardletBackgroundVM{" +
            "id=" + id +
            ", imageUrl='" + imageUrl + '\'' +
            ", textColor=" + textColor +
            ", text='" + text + '\'' +
            '}';
    }

    public void mapToCardletBackground(CardletBackground cardletBackgroundEntity){
        cardletBackgroundEntity.setImage(this.getImageUrl());
        cardletBackgroundEntity.setCaptionText(this.getText());
        cardletBackgroundEntity.setTextColor(this.isTextColor());
    }

    public void mapFromCardletBackground(CardletBackground cardletBackgroundEntity){
        this.id = cardletBackgroundEntity.getId();
        this.imageUrl = cardletBackgroundEntity.getImage();
        this.textColor = cardletBackgroundEntity.isTextColor();
        this.text = cardletBackgroundEntity.getCaptionText();
    }
}
