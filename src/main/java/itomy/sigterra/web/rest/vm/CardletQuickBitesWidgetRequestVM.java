package itomy.sigterra.web.rest.vm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Validated
@JsonIgnoreProperties(ignoreUnknown = true)
public class CardletQuickBitesWidgetRequestVM {

    private Long id;

    @NotNull
    @Size(min = 1, max = 30)
    private String title;

    @Size(max = 200)
    private String description;

    public CardletQuickBitesWidgetRequestVM() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
