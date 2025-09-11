package co.com.powerup2025.api.handlers;

import co.com.powerup2025.model.exception.enums.ErrorCode;
import co.com.powerup2025.model.exception.exceptions.BusinessException;
import co.com.powerup2025.model.logger.gateways.LoggerFactoryPort;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;


import co.com.powerup2025.api.mapper.UsuarioMapper;
import co.com.powerup2025.api.dtos.request.UserRequest;
import co.com.powerup2025.model.logger.gateways.LoggerRepository;
import co.com.powerup2025.model.user.gateways.IUserUseCase;
import reactor.core.publisher.Mono;

@Component
public class UsuarioHandler {
    private final IUserUseCase userUsecase;
    private final UsuarioMapper mapper;
    private final ReactiveErrorHandler errorHelper;
    private final LoggerRepository logger;

    public UsuarioHandler(IUserUseCase userUsecase, UsuarioMapper mapper, ReactiveErrorHandler errorHelper, LoggerFactoryPort logger) {
        this.userUsecase = userUsecase;
        this.mapper = mapper;
        this.errorHelper = errorHelper;
        this.logger = logger.getLogger(UsuarioHandler.class);
    }

    public Mono<ServerResponse> createUser(ServerRequest request) {


        return request.bodyToMono(UserRequest.class)
                .doFirst(() ->  logger.info("Iniciando creación de usuario"))
                .flatMap(dto -> logger.info(String.format("Datos recibidos: %s", dto)).thenReturn(dto))
                .map(mapper::toEntity)
                .flatMap(userUsecase::createUser)
                .map(mapper::toDto)
                .flatMap(userDto -> Mono
                        .fromRunnable(() -> logger.info(String.format("Usuario creado: %s", userDto)))
                        .then(ServerResponse.ok().bodyValue(userDto)))
                .doOnTerminate(() -> logger.info("Flujo finalizado"))
                .onErrorResume(errorHelper::handle);
    }

    public Mono<ServerResponse> getUser(ServerRequest request) {
        String email = request.queryParam("email").orElseThrow(() -> new BusinessException(ErrorCode.VAL_003));
        return userUsecase.findUserByEmail(email)
                .doOnSubscribe(e -> logger.info("Iniciando consulta de usuario por email"))
                .doOnEach(u -> logger.info(String.format("Datos recibidos: %s", email)))
                .doOnNext(u -> logger.info(String.format("Usuario encontrado: %s", u)))
                .flatMap(usuario -> ServerResponse.ok().bodyValue(usuario))
                .doOnTerminate(() -> logger.info("Flujo finalizado"))
                .onErrorResume(errorHelper::handle);
    }


}
