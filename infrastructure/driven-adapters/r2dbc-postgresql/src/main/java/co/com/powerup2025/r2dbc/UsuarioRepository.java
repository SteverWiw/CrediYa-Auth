package co.com.powerup2025.r2dbc;

import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import co.com.powerup2025.r2dbc.entity.UsuarioEntity;
import reactor.core.publisher.Mono;

public interface UsuarioRepository
        extends ReactiveCrudRepository<UsuarioEntity, Integer>, ReactiveQueryByExampleExecutor<UsuarioEntity> {

    Mono<Boolean> existsByEmail(String email);

    Mono<UsuarioEntity> findByEmail(String email);

}
