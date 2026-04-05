package co.com.powerup2025.r2dbc.repositories;

import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import co.com.powerup2025.r2dbc.entity.RoleEntity;
import reactor.core.publisher.Mono;

public interface RoleReactiveRepository extends ReactiveCrudRepository<RoleEntity, Long>, ReactiveQueryByExampleExecutor<RoleEntity> {

    Mono<RoleEntity> findByIdRole(Long idRole);

}