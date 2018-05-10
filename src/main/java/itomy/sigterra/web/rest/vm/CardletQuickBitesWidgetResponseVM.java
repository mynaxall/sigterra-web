package itomy.sigterra.web.rest.vm;

import itomy.sigterra.domain.CardletQuickBitesWidget;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CardletQuickBitesWidgetResponseVM {

    private Long id;

    @NotNull
    @Size(min = 1, max = 30)
    private String title;

    @Size(max = 200)
    private String description;

    public CardletQuickBitesWidgetResponseVM() {
        //For Jackson
    }

    public CardletQuickBitesWidgetResponseVM(CardletQuickBitesWidget entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.description = entity.getDescription();
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
