package co.com.powerup2025.api.handler;

import java.util.UUID;

import co.com.powerup2025.errorhelper.ReactiveErrorHelper;
import co.com.powerup2025.model.exception.enums.ErrorCode;
import co.com.powerup2025.model.exception.exceptions.BusinessException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;


import co.com.powerup2025.api.mapper.UsuarioMapper;
import co.com.powerup2025.api.request_dto.UsuarioRequestDTO;
import co.com.powerup2025.model.exception.gateways.LoggerPort;
import co.com.powerup2025.model.usuario.gateways.UsuarioService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

@Component
@RequiredArgsConstructor
public class UsuarioHandler {
    private final UsuarioService userUsecase;
    private final UsuarioMapper mapper;
    private final ReactiveErrorHelper errorHelper;
    private final LoggerPort logger;
    private final String traceId = UUID.randomUUID().toString();

    public Mono<ServerResponse> createUser(ServerRequest request) {


        return request.bodyToMono(UsuarioRequestDTO.class)
                .doOnSubscribe(s -> logger.info("Iniciando creación de usuario", traceId))
                .doOnNext(dto -> logger.info(String.format("Datos recibidos: %s", dto), traceId))
                .map(mapper::toEntity)
                .flatMap(userUsecase::createUser)
                .map(mapper::toDto)
                .flatMap(userDto -> Mono
                        .fromRunnable(() -> logger.info(String.format("Usuario creado: %s", userDto), traceId))
                        .then(ServerResponse.ok().bodyValue(userDto)))
                //.doOnError(e -> logger.error(traceId, e, e.getMessage()))
                .doOnTerminate(() -> logger.info("Flujo finalizado", traceId))
                .onErrorResume(errorHelper::handle)
                .contextWrite(Context.of("traceId", traceId));
    }

    public Mono<ServerResponse> getUser(ServerRequest request) {
        String email = request.queryParam("email").orElseThrow(() -> new BusinessException(ErrorCode.VAL_003));
        return userUsecase.getUserByEmail(email)
                .doOnSubscribe(e -> logger.info("Iniciando consulta de usuario por email", traceId))
                .doOnEach(u -> logger.info(String.format("Datos recibidos: %s", email), traceId))
                .doOnNext(u -> logger.info(String.format("Usuario encontrado: %s", u), traceId))
                .flatMap(usuario -> ServerResponse.ok().bodyValue(usuario))
                .doOnTerminate(() -> logger.info("Flujo finalizado", traceId))
                .onErrorResume(errorHelper::handle)
                .contextWrite(Context.of("traceId", traceId));
    }


}
