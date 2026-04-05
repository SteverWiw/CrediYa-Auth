package co.com.powerup2025.api.handlers;

import co.com.powerup2025.api.dtos.request.LoginRequest;
import co.com.powerup2025.api.dtos.response.TokenResponse;
import co.com.powerup2025.api.jwt.JWTUtil;
import co.com.powerup2025.model.auth.gateways.IAuthUseCase;
import co.com.powerup2025.model.logger.gateways.LoggerFactoryPort;
import co.com.powerup2025.model.logger.gateways.LoggerRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Set;

@Slf4j
@Component
public class AuthHandler {
    private final JWTUtil jwtUtil;
    private final IAuthUseCase authUseCase;
    private final LoggerRepository logger;
    private final ReactiveErrorHandler errorHelper;
    private final Validator validator;

    public AuthHandler(JWTUtil jwtUtil, IAuthUseCase authUseCase, PasswordEncoder passwordEncoder, LoggerFactoryPort logger, ReactiveErrorHandler errorHelper, Validator validator) {
        this.jwtUtil = jwtUtil;
        this.authUseCase = authUseCase;
        this.logger = logger.getLogger(AuthHandler.class);
        this.errorHelper = errorHelper;
        this.validator = validator;
    }


    public Mono<ServerResponse> auth(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(LoginRequest.class)
                .doFirst(() -> logger.info("Inicia login"))
                .flatMap(loginRequest -> {
                    logger.info(String.format("Datos entrada: %s", loginRequest));
                    Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);
                    return violations.isEmpty()
                            ? Mono.just(loginRequest)
                            : Mono.error(new ConstraintViolationException(violations));
                })
                .flatMap(loginRequest ->
                        authUseCase.authenticate(
                                loginRequest.email().trim().toLowerCase(),
                                loginRequest.password()
                        )
                )
                .flatMap(token -> ServerResponse.ok().bodyValue(new TokenResponse(token)))
                .doOnTerminate(() -> logger.info("Flujo finalizado"))
                .onErrorResume(errorHelper::handle);

    }

}

