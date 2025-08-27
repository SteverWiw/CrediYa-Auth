package co.com.powerup2025.usecase.usuario;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import co.com.powerup2025.model.exception.enums.ErrorCode;
import co.com.powerup2025.usecase.shared.BusinessException;
import co.com.powerup2025.usecase.spi.LoggerPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import co.com.powerup2025.model.usuario.Usuario;
import co.com.powerup2025.model.usuario.gateways.UsuarioRepository;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
class UsuarioUseCaseTest {

        @Mock
        private UsuarioRepository usuarioRepository;

        @Mock
        private LoggerPort logger;

        private UsuarioUseCase usuarioUseCase;

        private Usuario usuario;

        @BeforeEach
        void setUp() {
                usuarioUseCase = new UsuarioUseCase(usuarioRepository, logger);

                usuario = new Usuario();
                usuario.setIdUsuario(1);
                usuario.setEmail("test@test.com");
                usuario.setNombre("Test");
                usuario.setApellido("User");
                usuario.setDocumentoIdentidad(123456789L);
                usuario.setIdRol(1);
                usuario.setSalarioBase(BigDecimal.valueOf(150000));
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

                verify(logger).info("Iniciando creación de usuario");
                verify(logger).info("Usuario creado exitosamente");
                verify(usuarioRepository).existsByEmail("test@test.com");
                verify(usuarioRepository).save(any(Usuario.class));
        }

        @Test
        void shouldFailWhenEmailAlreadyExists() {
                when(usuarioRepository.existsByEmail("test@test.com"))
                        .thenReturn(Mono.just(true));

                Mono<Usuario> result = usuarioUseCase.createUser(usuario);

                StepVerifier.create(result)
                        .expectErrorMatches(throwable ->
                                throwable instanceof BusinessException &&
                                        ((BusinessException) throwable).getErrorCode() == ErrorCode.USR_002)
                        .verify();

                verify(logger).info("Iniciando creación de usuario");
                verify(usuarioRepository).existsByEmail("test@test.com");
                verify(usuarioRepository, never()).save(any());
        }
}