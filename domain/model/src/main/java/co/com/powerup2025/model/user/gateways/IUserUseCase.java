package co.com.powerup2025.model.user.gateways;

import co.com.powerup2025.model.user.User;
import reactor.core.publisher.Mono;


public interface IUserUseCase {

    Mono<User> createUser(User user);
    Mono<User> findUserByEmail(String email);



}
