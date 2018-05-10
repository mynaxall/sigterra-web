package itomy.sigterra.web.rest.vm;

import itomy.sigterra.domain.CardletTestimonialWidget;

public class CardletTestimonialWidgetResponseVM {

    private Long id;

    private String name;

    private String coName;

    private String designation;

    private String description;

    public CardletTestimonialWidgetResponseVM() {
        // For Jackson
    }

    public CardletTestimonialWidgetResponseVM(CardletTestimonialWidget cardletTestimonialWidget) {
        this.id = cardletTestimonialWidget.getId();
        this.name = cardletTestimonialWidget.getName();
        this.coName = cardletTestimonialWidget.getCoName();
        this.designation = cardletTestimonialWidget.getDesignation();
        this.description = cardletTestimonialWidget.getDescription();
        // For Jackson
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
