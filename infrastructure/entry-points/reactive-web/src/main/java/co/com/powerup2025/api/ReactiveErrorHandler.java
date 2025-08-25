package co.com.powerup2025.api;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;

import jakarta.validation.ConstraintViolationException;
import reactor.core.publisher.Mono;

@Component
public class ReactiveErrorHandler {

    public Mono<ServerResponse> handle(Throwable ex) {
        if (ex instanceof ConstraintViolationException violationEx) {
            return handleConstraintViolation(violationEx);
        } else if (ex instanceof IllegalArgumentException illegalArgEx) {
            return handleIllegalArgument(illegalArgEx);
        } else {
            return handleGenericError(ex);
        }
    }

    private Mono<ServerResponse> handleConstraintViolation(ConstraintViolationException ex) {
        List<Map<String, String>> violations = ex.getConstraintViolations().stream()
                .map(v -> Map.of(
                        "field", v.getPropertyPath().toString(),
                        "message", v.getMessage()))
                .toList();

        ErrorResponse response = ErrorResponse.of(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Validación fallida",
                violations);

        return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(response);
    }

    private Mono<ServerResponse> handleIllegalArgument(IllegalArgumentException ex) {
        ErrorResponse response = ErrorResponse.of(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage());
        return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(response);
    }

    private Mono<ServerResponse> handleGenericError(Throwable ex) {
        ErrorResponse response = ErrorResponse.of(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                ex.getMessage());
        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue(response);
    }
}
