package itomy.sigterra.web.rest.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for response entity building.
 */
public final class ResponseUtil {

    private ResponseUtil() {
    }

    public static ResponseEntity errorResponse(HttpStatus httpStatus, String errorMessage) {
        Map<String, String> errorObject = new HashMap<>();
        errorObject.put("message", errorMessage);

        return ResponseEntity.status(httpStatus).body(errorObject);
    }

    public static ResponseEntity okResponse() {
        return ResponseEntity.ok().build();
    }

    public static <T> ResponseEntity okResponse(T body) {
        return ResponseEntity.ok(body);
    }
}
