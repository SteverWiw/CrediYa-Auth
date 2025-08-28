package co.com.powerup2025.r2dbc;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;

import co.com.powerup2025.model.usuario.Usuario;
import co.com.powerup2025.r2dbc.entity.UsuarioEntity;
import co.com.powerup2025.r2dbc.helper.UsuarioAdapterOperations;
import reactor.core.publisher.Mono;

@Repository
public class UsuarioRepositoryAdapter
        extends UsuarioAdapterOperations<Usuario, UsuarioEntity, Integer, UsuarioRepository>
        implements co.com.powerup2025.model.usuario.gateways.UsuarioRepository {

    private final TransactionalOperator txOperator;

    public UsuarioRepositoryAdapter(UsuarioRepository repository, ObjectMapper mapper,
                                    TransactionalOperator txOperator) {
        super(repository, mapper, d -> mapper.mapBuilder(d, Usuario.UsuarioBuilder.class).build());
        this.txOperator = txOperator;
    }

    @Override
    public Mono<Boolean> existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public Mono<Usuario> save(Usuario usuario) {
        return Mono.just(usuario)
                .map(u -> mapper.mapBuilder(u, UsuarioEntity.UsuarioEntityBuilder.class).build())
                .flatMap(u -> repository.save(u))
                .map(entity -> mapper.mapBuilder(entity, Usuario.UsuarioBuilder.class).build())
                .as(txOperator::transactional);
    }

    /*
     * @Override
     * public Mono<Boolean> existsById(Integer id) {
     * return repository.existsById(id);
     * }
     * 
     * @Override
     * public Mono<Usuario> findByEmail(String email) {
     * return repository.findByEmail(email)
     * .map(entity -> mapper.map(entity, Usuario.class));
     * }
     * 
     * @Override
     * public Mono<Void> deleteById(Integer id) {
     * return repository.deleteById(id);
     * }
     */

}
