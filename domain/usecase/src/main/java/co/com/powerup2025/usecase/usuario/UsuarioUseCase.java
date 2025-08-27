package co.com.powerup2025.usecase.usuario;

import co.com.powerup2025.model.exception.enums.ErrorCode;
import co.com.powerup2025.model.usuario.Usuario;
import co.com.powerup2025.model.usuario.gateways.UsuarioRepository;
import co.com.powerup2025.usecase.shared.BusinessException;
import co.com.powerup2025.usecase.spi.LoggerPort;
import co.com.powerup2025.usecase.usuario.validator.UsuarioValidator;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import co.com.powerup2025.usecase.usuario.interfaces.UsuarioService;

import java.util.Map;

@RequiredArgsConstructor
public class UsuarioUseCase implements UsuarioService {
    private final UsuarioRepository usuarioRepository;

    private final LoggerPort logger;




    @Override
    public Mono<Boolean> userExistsByEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }



    @Override
    public Mono<Usuario> createUser(Usuario user) {
        return UsuarioValidator.validar(user)
                .doOnSubscribe(s -> logger.info("Iniciando creación de usuario"))
                .flatMap(v -> usuarioRepository.existsByEmail(user.getEmail()))
                .flatMap(exists -> Boolean.TRUE.equals(exists)
                    ?  Mono.error(new BusinessException(ErrorCode.USR_002))
                    : usuarioRepository.save(user)
                )
                .doOnSuccess(u -> logger.info("Usuario creado exitosamente"));
    }

}
