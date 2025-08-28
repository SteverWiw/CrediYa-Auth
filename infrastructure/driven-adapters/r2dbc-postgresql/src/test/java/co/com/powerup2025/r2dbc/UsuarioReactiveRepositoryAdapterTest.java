package co.com.powerup2025.r2dbc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;

import co.com.powerup2025.model.usuario.Usuario;
import co.com.powerup2025.r2dbc.entity.UsuarioEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class UsuarioReactiveRepositoryAdapterTest {

        @InjectMocks
        private UsuarioRepositoryAdapter adapter;

        @Mock
        private UsuarioRepository repository;

        @Mock
        private ObjectMapper mapper;

        private UsuarioEntity entity;
        private Usuario usuario;

        @BeforeEach
        void setup() {
                entity = UsuarioEntity.builder()
                                .idUsuario(1)
                                .email("test@test.com")
                                .build();

                usuario = Usuario.builder()
                                .idUsuario(1)
                                .email("test@test.com")
                                .nombre("Juan")
                                .build();
        }

        @Test
        void mustFindValueById() {
                when(repository.findById(1)).thenReturn(Mono.just(entity));
                when(mapper.mapBuilder(any(UsuarioEntity.class), any()))
                                .thenReturn(usuario.toBuilder());

                Mono<Usuario> result = adapter.findById(1);

                StepVerifier.create(result)
                                .assertNext(u -> {
                                        // validamos por campos en vez de equals
                                        assert u.getIdUsuario().equals(usuario.getIdUsuario());
                                        assert u.getEmail().equals(usuario.getEmail());
                                        assert u.getNombre().equals(usuario.getNombre());
                                })
                                .verifyComplete();
        }

        @Test
        void mustFindAllValues() {
                when(repository.findAll()).thenReturn(Flux.just(entity));
                when(mapper.mapBuilder(any(UsuarioEntity.class), any()))
                                .thenReturn(usuario.toBuilder());

                Flux<Usuario> result = adapter.findAll();

                StepVerifier.create(result)
                                .assertNext(u -> {
                                        assert u.getIdUsuario().equals(usuario.getIdUsuario());
                                        assert u.getEmail().equals(usuario.getEmail());
                                })
                                .verifyComplete();
        }

        @Test
        void mustSaveValue() {
                when(repository.save(any(UsuarioEntity.class))).thenReturn(Mono.just(entity));
                lenient().when(mapper.mapBuilder(
                                any(UsuarioEntity.class),
                                any())).thenReturn(usuario.toBuilder());

                Mono<Usuario> result = adapter.save(usuario);

                StepVerifier.create(result)
                                .assertNext(u -> {
                                        assert u.getIdUsuario().equals(usuario.getIdUsuario());
                                        assert u.getEmail().equals(usuario.getEmail());
                                })
                                .verifyComplete();
        }

}