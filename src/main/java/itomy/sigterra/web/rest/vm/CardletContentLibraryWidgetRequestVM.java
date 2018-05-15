package itomy.sigterra.web.rest.vm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Size;

@Validated
@JsonIgnoreProperties(ignoreUnknown = true)
public class CardletContentLibraryWidgetRequestVM {

    private Long id;

    @NotEmpty
    @Size(max = 100)
    private String title;

    private String coverImageUrl;

    private String uploadFileUrl;

    public CardletContentLibraryWidgetRequestVM() {
        //For Jackson
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

    public String getUploadFileUrl() {
        return uploadFileUrl;
    }

    public void setUploadFileUrl(String uploadFileUrl) {
        this.uploadFileUrl = uploadFileUrl;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }


}
