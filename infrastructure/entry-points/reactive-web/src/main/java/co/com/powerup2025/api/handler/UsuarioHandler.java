package co.com.powerup2025.api.handler;

import java.util.UUID;

import co.com.powerup2025.errorhelper.ReactiveErrorHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;


import co.com.powerup2025.api.mapper.UsuarioMapper;
import co.com.powerup2025.api.request_dto.UsuarioRequestDTO;
import co.com.powerup2025.model.usuario.gateways.LoggerPort;
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

    public Mono<ServerResponse> createUser(ServerRequest request) {

        String traceId = UUID.randomUUID().toString();

        return request.bodyToMono(UsuarioRequestDTO.class)
                .doOnSubscribe(s -> logger.info("Iniciando creación de usuario", traceId))
                .doOnNext(dto -> logger.info(String.format("Datos recibidos: %s",dto), traceId))
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

}
