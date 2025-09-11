package co.com.powerup2025.r2dbc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import co.com.powerup2025.model.user.User;
import co.com.powerup2025.r2dbc.adapters.UserReactiveRepositoryAdapter;
import co.com.powerup2025.r2dbc.repositories.UserReactiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;

import co.com.powerup2025.r2dbc.entity.UserEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class UserReactiveRepositoryAdapterTest {

        @InjectMocks
        private UserReactiveRepositoryAdapter adapter;

        @Mock
        private UserReactiveRepository repository;

        @Mock
        private ObjectMapper mapper;

        private UserEntity entity;
        private User user;

        @BeforeEach
        void setup() {
                entity = UserEntity.builder()
                                .idUsuario(1)
                                .email("test@test.com")
                                .build();

                user = User.builder()
                                .idUsuario(1)
                                .email("test@test.com")
                                .nombre("Juan")
                                .build();
        }

        @Test
        void mustFindValueById() {
                when(repository.findById(1)).thenReturn(Mono.just(entity));
                when(mapper.mapBuilder(any(UserEntity.class), any()))
                                .thenReturn(user.toBuilder());

                Mono<User> result = adapter.findById(1);

                StepVerifier.create(result)
                                .assertNext(u -> {
                                        // validamos por campos en vez de equals
                                        assert u.getIdUsuario().equals(user.getIdUsuario());
                                        assert u.getEmail().equals(user.getEmail());
                                        assert u.getNombre().equals(user.getNombre());
                                })
                                .verifyComplete();
        }

        @Test
        void mustFindAllValues() {
                when(repository.findAll()).thenReturn(Flux.just(entity));
                when(mapper.mapBuilder(any(UserEntity.class), any()))
                                .thenReturn(user.toBuilder());

                Flux<User> result = adapter.findAll();

                StepVerifier.create(result)
                                .assertNext(u -> {
                                        assert u.getIdUsuario().equals(user.getIdUsuario());
                                        assert u.getEmail().equals(user.getEmail());
                                })
                                .verifyComplete();
        }

        @Test
        void mustSaveValue() {
                when(repository.save(any(UserEntity.class))).thenReturn(Mono.just(entity));
                lenient().when(mapper.mapBuilder(
                                any(UserEntity.class),
                                any())).thenReturn(user.toBuilder());

                Mono<User> result = adapter.save(user);

                StepVerifier.create(result)
                                .assertNext(u -> {
                                        assert u.getIdUsuario().equals(user.getIdUsuario());
                                        assert u.getEmail().equals(user.getEmail());
                                })
                                .verifyComplete();
        }

}