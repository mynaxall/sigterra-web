package itomy.sigterra.domain;

import javax.persistence.*;
import java.util.Objects;

/*
   data for "Edit page", tab: background
 */


@Entity
@Table(name = "cardlet_background")
public class CardletBackground extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "image")
    private String image;

    //user can choose only black or white colour for the text Under the cardlet.
    @Column(name="text_color")
    private boolean textColor;

    @Column(name = "text")
    private String captionText;

    @OneToOne
    @JoinColumn(name = "cardlet_id")
    private Cardlet cardlet;

    public CardletBackground() {
    }

    public CardletBackground(String image, boolean textColor, String captionText, Cardlet cardlet) {
        this.image = image;
        this.textColor = textColor;
        this.captionText = captionText;
        this.cardlet = cardlet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CardletBackground that = (CardletBackground) o;
        if(that.id == null || id == null) {
            return false;
        }

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CardletBackground{" +
            "id=" + id +
            ", image='" + image + '\'' +
            ", textColor=" + textColor +
            ", captionText='" + captionText + '\'' +
            ", cardlet=" + cardlet +
            '}';
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isTextColor() {
        return textColor;
    }

    public void setTextColor(boolean textColor) {
        this.textColor = textColor;
    }

    public String getCaptionText() {
        return captionText;
    }

    public void setCaptionText(String captionText) {
        this.captionText = captionText;
    }

    public Long getId() {
        return id;
    }

    public Cardlet getCardlet() {
        return cardlet;
    }

    public void setCardlet(Cardlet cardlet) {
        this.cardlet = cardlet;
    }
}
