package itomy.sigterra.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A CardletContentLibraryWidget.
 */
@Entity
@Table(name = "cardlet_content_library_widget")
public class CardletContentLibraryWidget implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "title", length = 100, nullable = false)
    private String title;

    @NotEmpty
    @Column(name = "cover_image_url", nullable = false)
    private String coverImageUrl;

    @Column(name = "upload_file_url")
    private String uploadFileUrl;

    @ManyToOne
    @JsonIgnore
    private Cardlet cardlet;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public CardletContentLibraryWidget title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public CardletContentLibraryWidget coverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
        return this;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public Cardlet getCardlet() {
        return cardlet;
    }

    public CardletContentLibraryWidget cardlet(Cardlet cardlet) {
        this.cardlet = cardlet;
        return this;
    }

    public void setCardlet(Cardlet cardlet) {
        this.cardlet = cardlet;
    }

    public String getUploadFileUrl() {
        return uploadFileUrl;
    }

    public void setUploadFileUrl(String uploadFileUrl) {
        this.uploadFileUrl = uploadFileUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CardletContentLibraryWidget cardletContentLibraryWidget = (CardletContentLibraryWidget) o;
        if (cardletContentLibraryWidget.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cardletContentLibraryWidget.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CardletContentLibraryWidget{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", coverImageUrl='" + getCoverImageUrl() + "'" +
            "}";
    }
}
