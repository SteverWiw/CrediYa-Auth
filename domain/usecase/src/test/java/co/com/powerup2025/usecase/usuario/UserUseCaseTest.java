package co.com.powerup2025.usecase.usuario;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import co.com.powerup2025.model.logger.gateways.LoggerFactoryPort;
import co.com.powerup2025.model.user.User;
import co.com.powerup2025.usecase.user.UserUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.com.powerup2025.model.exception.enums.ErrorCode;
import co.com.powerup2025.model.user.gateways.UserRepository;
import co.com.powerup2025.model.exception.exceptions.BusinessException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class UserUseCaseTest {

        @Mock
        private UserRepository userRepository;

        @Mock
        private LoggerFactoryPort logger;

        private UserUseCase usuarioUseCase;

        private User user;

        @BeforeEach
        void setUp() {
                usuarioUseCase = new UserUseCase(userRepository, logger);

                user = new User();
                user.setIdUsuario(1);
                user.setEmail("prueba@test.com");
                user.setNombre("prueba");
                user.setApellido("prueba");
                user.setDocumentoIdentidad(123456789L);
                user.setIdRol(1);
                user.setSalarioBase(BigDecimal.valueOf(150000));
        }

       /* @Test
        void shouldReturnTrueWhenUserExistsByEmail() {
                when(usuarioRepository.existsByEmail("prueba@test.com"))
                                .thenReturn(Mono.just(true));

                Mono<Boolean> result = usuarioUseCase.userExistsByEmail("prueba@test.com");

                StepVerifier.create(result)
                                .expectNext(true)
                                .verifyComplete();

                verify(usuarioRepository).existsByEmail("prueba@test.com");
        }*/

        @Test
        void shouldCreateUserWhenEmailNotExists() {
                when(userRepository.existsByEmail("prueba@test.com"))
                                .thenReturn(Mono.just(false));

                when(userRepository.save(any(User.class)))
                                .thenReturn(Mono.just(user));

                Mono<User> result = usuarioUseCase.createUser(user);

                StepVerifier.create(result)
                                .expectNextMatches(u -> u.getEmail().equals("prueba@test.com"))
                                .verifyComplete();

                //verify(logger).info("Iniciando creación de usuario");
                //verify(logger).info("Usuario creado exitosamente");
                verify(userRepository).existsByEmail("prueba@test.com");
                verify(userRepository).save(any(User.class));
        }

        @Test
        void shouldFailWhenEmailAlreadyExists() {
                when(userRepository.existsByEmail("prueba@test.com"))
                                .thenReturn(Mono.just(true));

                Mono<User> result = usuarioUseCase.createUser(user);

                StepVerifier.create(result)
                                .expectErrorMatches(throwable -> throwable instanceof BusinessException &&
                                                ((BusinessException) throwable).getErrorCodes().equals(List.of(ErrorCode.USR_002)))
                                .verify();

                //verify(logger).info("Iniciando creación de usuario");
                verify(userRepository).existsByEmail("prueba@test.com");
                verify(userRepository, never()).save(any());
        }
}