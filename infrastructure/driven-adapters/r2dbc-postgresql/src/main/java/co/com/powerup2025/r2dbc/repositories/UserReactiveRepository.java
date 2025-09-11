package co.com.powerup2025.r2dbc.repositories;

import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import co.com.powerup2025.r2dbc.entity.UserEntity;
import reactor.core.publisher.Mono;

public interface UserReactiveRepository
        extends ReactiveCrudRepository<UserEntity, Integer>, ReactiveQueryByExampleExecutor<UserEntity> {

    Mono<Boolean> existsByEmail(String email);

    Mono<UserEntity> findByEmail(String email);

}
