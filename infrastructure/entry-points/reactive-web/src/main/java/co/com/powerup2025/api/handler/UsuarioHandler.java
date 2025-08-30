package co.com.powerup2025.api.handler;

import co.com.powerup2025.model.exception.enums.ErrorCode;
import co.com.powerup2025.model.exception.exceptions.BusinessException;
import co.com.powerup2025.model.exception.gateways.LoggerFactoryPort;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;


import co.com.powerup2025.api.mapper.UsuarioMapper;
import co.com.powerup2025.api.request_dto.UsuarioRequestDTO;
import co.com.powerup2025.model.exception.gateways.LoggerPort;
import co.com.powerup2025.model.usuario.gateways.UsuarioService;
import reactor.core.publisher.Mono;

@Component
public class UsuarioHandler {
    private final UsuarioService userUsecase;
    private final UsuarioMapper mapper;
    private final ReactiveErrorHandler errorHelper;
    private final LoggerPort logger;

    public UsuarioHandler(UsuarioService userUsecase, UsuarioMapper mapper, ReactiveErrorHandler errorHelper, LoggerFactoryPort logger) {
        this.userUsecase = userUsecase;
        this.mapper = mapper;
        this.errorHelper = errorHelper;
        this.logger = logger.getLogger(UsuarioHandler.class);
    }

    public Mono<ServerResponse> createUser(ServerRequest request) {


        return request.bodyToMono(UsuarioRequestDTO.class)
                .doFirst(() ->  logger.info("Iniciando creación de usuario"))
                .flatMap(dto -> logger.info(String.format("Datos recibidos: %s", dto)).thenReturn(dto))
                .map(mapper::toEntity)
                .flatMap(userUsecase::createUser)
                .map(mapper::toDto)
                .flatMap(userDto -> Mono
                        .fromRunnable(() -> logger.info(String.format("Usuario creado: %s", userDto)))
                        .then(ServerResponse.ok().bodyValue(userDto)))
                //.doOnError(e -> logger.error(traceId, e, e.getMessage()))
                .doOnTerminate(() -> logger.info("Flujo finalizado"))
                .onErrorResume(errorHelper::handle);
    }

    public Mono<ServerResponse> getUser(ServerRequest request) {
        String email = request.queryParam("email").orElseThrow(() -> new BusinessException(ErrorCode.VAL_003));
        return userUsecase.getUserByEmail(email)
                .doOnSubscribe(e -> logger.info("Iniciando consulta de usuario por email"))
                .doOnEach(u -> logger.info(String.format("Datos recibidos: %s", email)))
                .doOnNext(u -> logger.info(String.format("Usuario encontrado: %s", u)))
                .flatMap(usuario -> ServerResponse.ok().bodyValue(usuario))
                .doOnTerminate(() -> logger.info("Flujo finalizado"))
                .onErrorResume(errorHelper::handle);
    }


}
