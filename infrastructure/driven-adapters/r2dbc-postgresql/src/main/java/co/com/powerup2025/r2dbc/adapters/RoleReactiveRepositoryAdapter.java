package co.com.powerup2025.r2dbc.adapters;


import co.com.powerup2025.model.logger.gateways.LoggerFactoryPort;
import co.com.powerup2025.model.logger.gateways.LoggerRepository;
import co.com.powerup2025.model.role.Role;
import co.com.powerup2025.model.role.gateways.RoleRepository;
import co.com.powerup2025.r2dbc.entity.RoleEntity;
import co.com.powerup2025.r2dbc.helper.ReactiveAdapterOperations;
import co.com.powerup2025.r2dbc.repositories.RoleReactiveRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Mono;

@Repository
public class RoleReactiveRepositoryAdapter extends ReactiveAdapterOperations<Role, RoleEntity, Long, RoleReactiveRepository> implements RoleRepository {

    private final LoggerRepository logger;

    public RoleReactiveRepositoryAdapter(RoleReactiveRepository repository, ObjectMapper mapper, LoggerFactoryPort logger) {
        super(repository, mapper, d -> mapper.map(d, Role.RoleBuilder.class).build());

       this.logger = logger.getLogger(RoleReactiveRepositoryAdapter.class);
    }

    @Override
    public Mono<Role> findByIdRole(Long idRole) {
        return repository.findByIdRole(idRole)
                .map(r -> {
                    logger.info(String.format("Entidad recibida para mapeo: %s", r));
                    Role role = Role.builder()
                            .idRole(r.getIdRole())
                            .nombre(r.getNombre())
                            .descripcion(r.getDescripcion())
                            .build();
                    logger.info(String.format("Mapper manual devolvió entidad: %s", role));
                    return role;
                });
    }
}

