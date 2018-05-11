package itomy.sigterra.web.rest.vm;

import itomy.sigterra.domain.CardletContentLibraryWidget;

public class CardletContentLibraryWidgetResponseVM {

    private Long id;

    private String title;

    private String coverImageUrl;

    private String uploadFileUrl;

    public CardletContentLibraryWidgetResponseVM() {
        //For Jackson
    }

    public CardletContentLibraryWidgetResponseVM(CardletContentLibraryWidget widget) {
        this.id = widget.getId();
        this.title = widget.getTitle();
        this.coverImageUrl = widget.getCoverImageUrl();
        this.uploadFileUrl = widget.getUploadFileUrl();
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

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public String getUploadFileUrl() {
        return uploadFileUrl;
    }

    public void setUploadFileUrl(String uploadFileUrl) {
        this.uploadFileUrl = uploadFileUrl;
    }
}
