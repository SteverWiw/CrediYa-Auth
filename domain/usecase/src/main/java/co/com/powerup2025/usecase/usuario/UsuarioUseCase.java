package co.com.powerup2025.usecase.usuario;

import co.com.powerup2025.model.usuario.Usuario;
import co.com.powerup2025.model.usuario.gateways.UsuarioRepository;
import lombok.RequiredArgsConstructor;
//import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UsuarioUseCase implements UsuarioService {
    private final UsuarioRepository usuarioRepository;

    @Override
    public Mono<Boolean> userExistsByEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    @Override
    public Mono<Boolean> userExistsById(Integer id) {
        return usuarioRepository.existsById(id);
    }

    @Override
    public Mono<Usuario> createUser(Usuario user) {
        return usuarioRepository.existsByEmail(user.getEmail())
                // .doOnSubscribe(s -> log.info("Verificando si el email ya está en uso: {}",
                // user.getEmail()))
                .flatMap(exists -> Mono.defer(() -> Boolean.TRUE.equals(exists)
                        ? Mono.error(new RuntimeException("El email ya está en uso"))
                        : usuarioRepository.save(user)))
                .flatMap(Mono::just);

    }

    @Override
    public Mono<Usuario> getUserById(Integer id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public Mono<Usuario> getUserByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Override
    public Mono<Void> deleteUserById(Integer id) {
        return usuarioRepository.deleteById(id);
    }

    @Override
    public Flux<Usuario> getAllUser() {
        return usuarioRepository.findAll();
    }

    @Override
    public Mono<Usuario> updateUser(Integer id, Usuario user) {
        return usuarioRepository.findById(id)
                .flatMap(usuario -> usuarioRepository.save(user))
                .switchIfEmpty(Mono.error(new RuntimeException("Usuario no encontrado")));
    }
}
