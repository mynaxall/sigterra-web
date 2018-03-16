package itomy.sigterra.web.rest.vm;

import java.util.List;
import java.util.stream.Collectors;

public class ListFilesPathsResponseVM {
    private final List<PathFileVM> listFilesPaths;

    public ListFilesPathsResponseVM(List<String> listFilesPaths) {
        this.listFilesPaths = listFilesPaths.stream().map(PathFileVM::new).collect(Collectors.toList());
    }

    public List<PathFileVM> getListFilesPaths() {
        return listFilesPaths;
    }

}
