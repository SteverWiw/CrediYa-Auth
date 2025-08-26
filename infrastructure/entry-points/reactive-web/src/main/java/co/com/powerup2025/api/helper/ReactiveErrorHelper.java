package co.com.powerup2025.api.helper;

import co.com.powerup2025.api.mapper.ErrorCodeMapper;
import co.com.powerup2025.api.response_dto.ErrorResponse;
import co.com.powerup2025.model.exception.enums.ErrorCode;
import co.com.powerup2025.model.exception.iErrorCode;
import co.com.powerup2025.usecase.shared.BusinessException;
import co.com.powerup2025.usecase.spi.LoggerPort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class ReactiveErrorHelper {
    private final LoggerPort logger;

    public ReactiveErrorHelper(LoggerPort logger) {
        this.logger = logger;
    }

    public <T> Mono<ServerResponse> handle(Throwable ex) {
        return Mono.deferContextual(ctx -> {
            String traceId = ctx.getOrDefault("traceId", "NO_TRACE");

            iErrorCode error = (ex instanceof BusinessException be)
                    ? be.getErrorCode()
                    : ErrorCode.SYS_001;

            HttpStatus status = ErrorCodeMapper.mapToHttpStatus(error);

            logger.error(error.message(),ex,traceId);

            ErrorResponse response = new ErrorResponse(
                    error.code(),
                    error.message(),
                    error.module().name(),
                    error.severity().name(),
                    traceId
            );

            return ServerResponse.status(status).bodyValue(response);
        });
    }
}