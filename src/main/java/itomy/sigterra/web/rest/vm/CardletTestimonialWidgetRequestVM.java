package itomy.sigterra.web.rest.vm;


import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CardletTestimonialWidgetRequestVM {

    private Long id;

    @NotNull
    private Long cardletId;

    @NotEmpty
    private String name;

    private String coName;

    private String designation;

    @Size(max = 30)
    @NotEmpty
    private String description;

    public CardletTestimonialWidgetRequestVM() {
        //For Jackson
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCardletId() {
        return cardletId;
    }

    public void setCardletId(Long cardletId) {
        this.cardletId = cardletId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoName() {
        return coName;
    }

    public void setCoName(String coName) {
        this.coName = coName;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
