package co.com.powerup2025.usecase.usuario;

import co.com.powerup2025.model.usuario.Usuario;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UsuarioService {

    Mono<Boolean> userExistsByEmail(String email);

    Mono<Boolean> userExistsById(Integer id);

    Mono<Usuario> createUser(Usuario user);

    Mono<Usuario> getUserById(Integer id);

    Mono<Usuario> getUserByEmail(String email);

    Mono<Void> deleteUserById(Integer id);

    Flux<Usuario> getAllUser();

    Mono<Usuario> updateUser(Integer id, Usuario user);
}
