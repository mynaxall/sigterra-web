package itomy.sigterra.web.rest.vm;

public class ContentLibraryWidgetStatisticResponseVM {

    private Long likes;
    private Long views;

    public Long getLikes() {
        return likes;
    }

    public void setLikes(Long likes) {
        this.likes = likes;
    }

    public Long getViews() {
        return views;
    }

    public void setViews(Long views) {
        this.views = views;
    }

    public ContentLibraryWidgetStatisticResponseVM(Long likes, Long views) {
        this.likes = likes;
        this.views = views;
    }
}
