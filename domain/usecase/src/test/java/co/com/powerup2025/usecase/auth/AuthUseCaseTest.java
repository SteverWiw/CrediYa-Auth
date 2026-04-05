package co.com.powerup2025.usecase.auth;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.com.powerup2025.model.auth.gateways.PasswordEncoderPort;
import co.com.powerup2025.model.auth.gateways.TokenGeneratorPort;
import co.com.powerup2025.model.exception.enums.ErrorCode;
import co.com.powerup2025.model.exception.exceptions.BusinessException;
import co.com.powerup2025.model.exception.exceptions.InvalidCredentialsException;
import co.com.powerup2025.model.logger.gateways.LoggerFactoryPort;
import co.com.powerup2025.model.logger.gateways.LoggerRepository;
import co.com.powerup2025.model.role.Role;
import co.com.powerup2025.model.role.gateways.RoleRepository;
import co.com.powerup2025.model.user.User;
import co.com.powerup2025.model.user.gateways.IUserUseCase;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class AuthUseCaseTest {

    @Mock
    private IUserUseCase userUseCase;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoderPort passwordEncoder;

    @Mock
    private TokenGeneratorPort tokenGenerator;

    @Mock
    private LoggerFactoryPort loggerFactory;

    @Mock
    private LoggerRepository logger;

    private AuthUseCase authUseCase;

    private final String rawPassword = "123456";
    private final String encodedPassword = "$2a$10$abc123";
    private final String token = "jwt-token";

    private User user;
    private Role role;

    @BeforeEach
    void setUp() {
        when(loggerFactory.getLogger(AuthUseCase.class)).thenReturn(logger);
        when(logger.info(any())).thenReturn(Mono.empty());

        authUseCase = new AuthUseCase(userUseCase, roleRepository, passwordEncoder, tokenGenerator, loggerFactory);

        role = Role.builder()
                .idRole(1L)
                .nombre("ADMIN")
                .build();

        user = User.builder()
                .email("test@mail.com")
                .password(encodedPassword)
                .idRol(1L)
                .build();
    }

    @Test
    void authenticate_Success() {
        when(userUseCase.findUserByEmail(user.getEmail())).thenReturn(Mono.just(user));
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);
        when(roleRepository.findByIdRole(user.getIdRol())).thenReturn(Mono.just(role));
        when(tokenGenerator.generateToken(user.getEmail(), role.getNombre())).thenReturn(token);

        StepVerifier.create(authUseCase.authenticate(user.getEmail(), rawPassword))
                .expectNext(token)
                .verifyComplete();
    }

    @Test
    void authenticate_UserNotFound() {
        when(userUseCase.findUserByEmail(user.getEmail())).thenReturn(Mono.empty());

        StepVerifier.create(authUseCase.authenticate(user.getEmail(), rawPassword))
                .expectErrorMatches(throwable -> throwable instanceof BusinessException
                && ((BusinessException) throwable).getErrorCodes().equals(List.of(ErrorCode.USR_001)))
                .verify();
    }

    @Test
    void authenticate_InvalidPassword() {
        when(userUseCase.findUserByEmail(user.getEmail())).thenReturn(Mono.just(user));
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(false);

        StepVerifier.create(authUseCase.authenticate(user.getEmail(), rawPassword))
                .expectError(InvalidCredentialsException.class)
                .verify();
    }

}
