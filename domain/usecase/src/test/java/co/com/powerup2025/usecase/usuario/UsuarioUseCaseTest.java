package co.com.powerup2025.usecase.usuario;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.com.powerup2025.model.exception.enums.ErrorCode;
import co.com.powerup2025.model.usuario.Usuario;
import co.com.powerup2025.model.usuario.gateways.UsuarioRepository;
import co.com.powerup2025.model.exception.exceptions.BusinessException;
import co.com.powerup2025.model.exception.gateways.LoggerPort;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

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
                usuario.setEmail("prueba@test.com");
                usuario.setNombre("prueba");
                usuario.setApellido("prueba");
                usuario.setDocumentoIdentidad(123456789L);
                usuario.setIdRol(1);
                usuario.setSalarioBase(BigDecimal.valueOf(150000));
        }

        @Test
        void shouldReturnTrueWhenUserExistsByEmail() {
                when(usuarioRepository.existsByEmail("prueba@test.com"))
                                .thenReturn(Mono.just(true));

                Mono<Boolean> result = usuarioUseCase.userExistsByEmail("prueba@test.com");

                StepVerifier.create(result)
                                .expectNext(true)
                                .verifyComplete();

                verify(usuarioRepository).existsByEmail("prueba@test.com");
        }

        @Test
        void shouldCreateUserWhenEmailNotExists() {
                when(usuarioRepository.existsByEmail("prueba@test.com"))
                                .thenReturn(Mono.just(false));

                when(usuarioRepository.save(any(Usuario.class)))
                                .thenReturn(Mono.just(usuario));

                Mono<Usuario> result = usuarioUseCase.createUser(usuario);

                StepVerifier.create(result)
                                .expectNextMatches(u -> u.getEmail().equals("prueba@test.com"))
                                .verifyComplete();

                verify(logger).info("Iniciando creación de usuario");
                verify(logger).info("Usuario creado exitosamente");
                verify(usuarioRepository).existsByEmail("prueba@test.com");
                verify(usuarioRepository).save(any(Usuario.class));
        }

        @Test
        void shouldFailWhenEmailAlreadyExists() {
                when(usuarioRepository.existsByEmail("prueba@test.com"))
                                .thenReturn(Mono.just(true));

                Mono<Usuario> result = usuarioUseCase.createUser(usuario);

                StepVerifier.create(result)
                                .expectErrorMatches(throwable -> throwable instanceof BusinessException &&
                                                ((BusinessException) throwable).getErrorCodes().equals(List.of(ErrorCode.USR_002)))
                                .verify();

                verify(logger).info("Iniciando creación de usuario");
                verify(usuarioRepository).existsByEmail("prueba@test.com");
                verify(usuarioRepository, never()).save(any());
        }
}