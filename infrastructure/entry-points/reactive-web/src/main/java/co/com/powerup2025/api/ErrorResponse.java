package co.com.powerup2025.api;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public record ErrorResponse(
        int status,
        String error,
        String message,
        List<Map<String, String>> violations,
        Instant timestamp) {
    public static ErrorResponse of(int status, String error, String message) {
        return new ErrorResponse(status, error, message, null, Instant.now());
    }

    public static ErrorResponse of(int status, String error, String message, List<Map<String, String>> violations) {
        return new ErrorResponse(status, error, message, violations, Instant.now());
    }
}
