package itomy.sigterra.service.exception;

import org.springframework.http.HttpStatus;

public class ResponseErrorException extends RuntimeException {

    private final HttpStatus httpStatus;

    public ResponseErrorException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
