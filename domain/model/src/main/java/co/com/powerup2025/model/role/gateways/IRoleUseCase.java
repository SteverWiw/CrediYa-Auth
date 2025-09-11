package co.com.powerup2025.model.role.gateways;

import co.com.powerup2025.model.role.Role;
import reactor.core.publisher.Mono;

public interface IRoleUseCase {
    Mono<Role> findByIdRole(Long idRole);
}
