package co.com.powerup2025.model.usuario.gateways;

import co.com.powerup2025.model.usuario.Usuario;
import reactor.core.publisher.Mono;

public interface UsuarioRepository {
    Mono<Boolean> existsByEmail(String email);

    // Mono<Boolean> existsById(Integer id);

    Mono<Usuario> save(Usuario usuario);

    // Mono<Usuario> findById(Integer id);

    // Mono<Usuario> findByEmail(String email);

    // Mono<Void> deleteById(Integer id);

    // Flux<Usuario> findAll();
}
