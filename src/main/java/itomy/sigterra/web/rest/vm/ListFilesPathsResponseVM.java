package itomy.sigterra.web.rest.vm;

import java.util.List;
import java.util.stream.Collectors;

public class ListFilesPathsResponseVM {
    private final List<Path> listFilesPaths;

    public ListFilesPathsResponseVM(List<String> listFilesPaths) {
        this.listFilesPaths = listFilesPaths.stream().map(Path::new).collect(Collectors.toList());
    }

    public List<Path> getListFilesPaths() {
        return listFilesPaths;
    }

    class Path {
        String url;

        public Path(String url) {
            if (url != null) {
                this.url = url.replaceAll("\\\\", "/");
            }
        }

        public String getUrl() {
            return url;
        }
    }
}
