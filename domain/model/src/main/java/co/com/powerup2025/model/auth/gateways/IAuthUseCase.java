package co.com.powerup2025.model.auth.gateways;


import reactor.core.publisher.Mono;

public interface IAuthUseCase {
    Mono<String> authenticate(String email, String rawPassword);
}
