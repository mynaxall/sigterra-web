package itomy.sigterra.web.rest.vm;

import javax.validation.constraints.NotNull;

class PathFileVM {
    @NotNull
    private String url;

    public PathFileVM(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
