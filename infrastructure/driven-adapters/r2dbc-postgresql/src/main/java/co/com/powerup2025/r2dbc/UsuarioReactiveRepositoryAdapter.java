package co.com.powerup2025.r2dbc;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

import co.com.powerup2025.model.usuario.Usuario;
import co.com.powerup2025.model.usuario.gateways.UsuarioRepository;
import co.com.powerup2025.r2dbc.entity.UsuarioEntity;
import co.com.powerup2025.r2dbc.helper.ReactiveAdapterOperations;
import reactor.core.publisher.Mono;

@Repository
public class UsuarioReactiveRepositoryAdapter
        extends ReactiveAdapterOperations<Usuario, UsuarioEntity, Integer, UsuarioReactiveRepository>
        implements UsuarioRepository {
    public UsuarioReactiveRepositoryAdapter(UsuarioReactiveRepository repository, ObjectMapper mapper) {
        /**
         * Could be use mapper.mapBuilder if your domain model implement builder pattern
         * super(repository, mapper, d ->
         * mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         * Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.mapBuilder(d, Usuario.UsuarioBuilder.class).build());
    }

    @Override
    public Mono<Boolean> existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public Mono<Boolean> existsById(Integer id) {
        return repository.existsById(id);
    }

    @Override
    public Mono<Usuario> findByEmail(String email) {
        return repository.findByEmail(email)
                .map(entity -> mapper.map(entity, Usuario.class));
    }

    @Override
    public Mono<Void> deleteById(Integer id) {
        return repository.deleteById(id);
    }

}
