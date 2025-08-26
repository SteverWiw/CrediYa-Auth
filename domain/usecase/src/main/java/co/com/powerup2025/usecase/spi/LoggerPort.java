package co.com.powerup2025.usecase.spi;

import reactor.core.publisher.Mono;

public interface LoggerPort {
    Mono<Void> info(String message);
    Mono<Void> info(String message, String contextId);
    Mono<Void> warn(String message);
    Mono<Void> error(String message, Throwable throwable);
    Mono<Void> error(String message, Throwable throwable, String contextId);

}
