package co.com.powerup2025.usecase.usuario;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.com.powerup2025.model.usuario.Usuario;
import co.com.powerup2025.model.usuario.gateways.UsuarioRepository;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class UsuarioUseCaseTest {

        @Mock
        private UsuarioRepository usuarioRepository;

        @InjectMocks
        private UsuarioUseCase usuarioUseCase;

        private Usuario usuario;

        @BeforeEach
        void setup() {
                usuario = new Usuario();
                usuario.setIdUsuario(1);
                usuario.setEmail("test@test.com");
        }

        @Test
        void shouldReturnTrueWhenUserExistsByEmail() {

                when(usuarioRepository.existsByEmail("test@test.com"))
                                .thenReturn(Mono.just(true));

                Mono<Boolean> result = usuarioUseCase.userExistsByEmail("test@test.com");

                StepVerifier.create(result)
                                .expectNext(true)
                                .verifyComplete();

                verify(usuarioRepository).existsByEmail("test@test.com");
        }

        @Test
        void shouldCreateUserWhenEmailNotExists() {

                when(usuarioRepository.existsByEmail("test@test.com"))
                                .thenReturn(Mono.just(false));

                when(usuarioRepository.save(any(Usuario.class)))
                                .thenReturn(Mono.just(usuario));

                Mono<Usuario> result = usuarioUseCase.createUser(usuario);

                StepVerifier.create(result)
                                .expectNextMatches(u -> u.getEmail().equals("test@test.com"))
                                .verifyComplete();

                verify(usuarioRepository).existsByEmail("test@test.com");

                verify(usuarioRepository).save(usuario);
        }

        @Test
        void shouldFailWhenEmailAlreadyExists() {

                when(usuarioRepository.existsByEmail("test@test.com"))
                                .thenReturn(Mono.just(true));

                Mono<Usuario> result = usuarioUseCase.createUser(usuario);

                StepVerifier.create(result)
                                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                                                throwable.getMessage().equals("El email ya está en uso"))
                                .verify();

                verify(usuarioRepository).existsByEmail("test@test.com");
                verify(usuarioRepository, never()).save(any());
        }
}
