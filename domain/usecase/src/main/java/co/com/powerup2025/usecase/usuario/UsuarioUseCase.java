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

//TODO: agregar logs
//TODO: manejar errores que se sacaran de la dto

//TODO: agregar validaciones de negocio por enum 

//TODO: implementar el logger


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
                .flatMap(exists -> {
                    if (Boolean.TRUE.equals(exists)) {
                        return Mono.error(new BusinessException(ErrorCode.USR_002));
                    }
                    return usuarioRepository.save(user);
                })
                .doOnSuccess(u -> logger.info("Usuario creado exitosamente"));
    }


    /*@Override
    public Mono<Usuario> createUser(Usuario user) {
        return Mono.defer(() -> UsuarioValidator.validar(user))
                .doOnSubscribe(s -> logger.info("Iniciando creación de usuario"))
                .flatMap(validUser -> usuarioRepository.existsByEmail(validUser.getEmail())
                        .flatMap(exists -> {
                            if (Boolean.TRUE.equals(exists)) {
                                var exception = new BusinessException(ErrorCode.USR_002);
                                logger.error("El email ya está en uso", exception);
                                return Mono.error(exception);
                            }
                            return usuarioRepository.save(validUser);
                        })
                )
                .doOnSuccess(u -> logger.info("Usuario creado exitosamente"))
                .doOnError(e -> logger.error("Error creando usuario", e));
    }*/

    /*
     * @Override
     * public Mono<Boolean> userExistsById(Integer id) {
     * return usuarioRepository.existsById(id);
     * }
     */

    /*
     * @Override
     * public Mono<Usuario> getUserById(Integer id) {
     * return usuarioRepository.findById(id);
     * }
     * 
     * @Override
     * public Mono<Usuario> getUserByEmail(String email) {
     * return usuarioRepository.findByEmail(email);
     * }
     * 
     * @Override
     * public Mono<Void> deleteUserById(Integer id) {
     * return usuarioRepository.deleteById(id);
     * }
     * 
     * @Override
     * public Flux<Usuario> getAllUser() {
     * return usuarioRepository.findAll();
     * }
     * 
     * @Override
     * public Mono<Usuario> updateUser(Integer id, Usuario user) {
     * return usuarioRepository.findById(id)
     * .flatMap(usuario -> usuarioRepository.save(user))
     * .switchIfEmpty(Mono.error(new RuntimeException("Usuario no encontrado")));
     * }
     */
}
