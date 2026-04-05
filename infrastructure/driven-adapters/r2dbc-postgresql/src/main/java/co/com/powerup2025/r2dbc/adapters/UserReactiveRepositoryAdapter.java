package co.com.powerup2025.r2dbc.adapters;

import co.com.powerup2025.r2dbc.repositories.UserReactiveRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;

import co.com.powerup2025.model.user.User;
import co.com.powerup2025.r2dbc.entity.UserEntity;
import co.com.powerup2025.r2dbc.helper.ReactiveAdapterOperations;
import reactor.core.publisher.Mono;
import co.com.powerup2025.model.user.gateways.UserRepository;

@Repository
public class UserReactiveRepositoryAdapter
        extends ReactiveAdapterOperations<User, UserEntity, Integer, UserReactiveRepository>
        implements UserRepository {

    private final TransactionalOperator txOperator;

    public UserReactiveRepositoryAdapter(UserReactiveRepository repository, ObjectMapper mapper,
                                         TransactionalOperator txOperator) {
        super(repository, mapper, d -> mapper.mapBuilder(d, User.UserBuilder.class).build());
        this.txOperator = txOperator;
    }

    @Override
    public Mono<Boolean> existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public Mono<User> getUserByEmail(String email) {
        return Mono.just(email)
                .flatMap(repository::findByEmail)
                .map(entity -> mapper.mapBuilder(entity, User.UserBuilder.class).build());
    }

    @Override
    public Mono<User> save(User user) {
        return Mono.just(user)
                .map(u -> mapper.mapBuilder(u, UserEntity.UserEntityBuilder.class).build())
                .flatMap(u -> repository.save(u))
                .map(entity -> mapper.mapBuilder(entity, User.UserBuilder.class).build())
                .as(txOperator::transactional);
    }

}
