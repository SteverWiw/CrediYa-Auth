package co.com.powerup2025.model.user.gateways;

import co.com.powerup2025.model.user.User;
import reactor.core.publisher.Mono;

public interface UserRepository {

    Mono<Boolean> existsByEmail(String email);

    Mono<User> getUserByEmail(String email);

    Mono<User> save(User user);

}
