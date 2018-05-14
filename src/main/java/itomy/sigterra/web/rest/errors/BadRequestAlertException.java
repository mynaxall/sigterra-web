package itomy.sigterra.web.rest.errors;

public class BadRequestAlertException extends RuntimeException {

    private final String entityName;

    private final String errorKey;

    public BadRequestAlertException(String entityName, String errorKey) {
        super(errorKey);
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
