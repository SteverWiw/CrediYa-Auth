package co.com.powerup2025.api;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import co.com.powerup2025.api.mapper.UsuarioMapper;
import co.com.powerup2025.api.request_dto.UsuarioRequestDTO;
import co.com.powerup2025.usecase.usuario.UsuarioService;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class UsuarioHandler {
    private final UsuarioService userUsecase;
    private final UsuarioMapper mapper;
    private final Validator validator;
    private final ReactiveErrorHandler errorHandler;

    @Transactional
    public Mono<ServerResponse> createUser(ServerRequest request) {
        return request.bodyToMono(UsuarioRequestDTO.class)
                .doOnSubscribe(s -> log.info("Iniciando creación de usuario"))
                .flatMap(dto -> Mono.defer(() -> validator.validate(dto).isEmpty()
                        ? Mono.just(dto)
                        : Mono.error(new ConstraintViolationException(validator.validate(dto)))))
                .map(mapper::toEntity)
                .flatMap(userUsecase::createUser)
                .map(mapper::toDto)
                .flatMap(userDto -> {
                    log.info("Usuario creado exitosamente: {}", userDto);
                    return ServerResponse.ok().bodyValue(userDto);
                })
                .doOnError(e -> log.error("Error creando usuario: {}", e.getMessage(), e))
                .doOnTerminate(() -> log.info("Flujo finalizado"))
                .onErrorResume(errorHandler::handle);
    }

}
