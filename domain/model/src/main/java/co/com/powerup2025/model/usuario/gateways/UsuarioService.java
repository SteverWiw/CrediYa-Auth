package co.com.powerup2025.model.usuario.gateways;

import co.com.powerup2025.model.usuario.Usuario;
import reactor.core.publisher.Mono;


public interface UsuarioService {

    Mono<Boolean> userExistsByEmail(String email);

    Mono<Usuario> createUser(Usuario user);

}
