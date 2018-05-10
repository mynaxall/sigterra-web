package itomy.sigterra.web.rest.errors;

import java.util.HashMap;
import java.util.Map;

public class BadRequestAlertException extends RuntimeException {

    private final String entityName;

    private final String errorKey;


    public BadRequestAlertException(String entityName, String errorKey) {
        this.entityName = entityName;
        this.errorKey = errorKey;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getErrorKey() {
        return errorKey;
    }

    public ParameterizedErrorVM getErrorVM() {
        return new ParameterizedErrorVM(errorKey, entityName);
    }
}
