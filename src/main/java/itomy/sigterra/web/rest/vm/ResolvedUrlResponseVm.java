package itomy.sigterra.web.rest.vm;

public class ResolvedUrlResponseVm {
    private String title;
    private String description;
    private String imageUrl;

    public ResolvedUrlResponseVm() {
        // for Jackson
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
