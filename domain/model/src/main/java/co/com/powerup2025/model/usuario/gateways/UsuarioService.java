package co.com.powerup2025.model.usuario.gateways;

import co.com.powerup2025.model.usuario.Usuario;
import reactor.core.publisher.Mono;


public interface UsuarioService {

    Mono<Usuario> getUserByEmail(String email);

    Mono<Usuario> createUser(Usuario user);

}
