package co.com.powerup2025.usecase.usuario;

import co.com.powerup2025.model.usuario.Usuario;
import co.com.powerup2025.model.usuario.gateways.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
class UsuarioUseCaseTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    private UsuarioUseCase usuarioUseCase;

    private Usuario usuarioEjemplo;

    @BeforeEach
    void setUp() {
        usuarioUseCase = new UsuarioUseCase(usuarioRepository);

        usuarioEjemplo = Usuario.builder()
                .idUsuario(1)
                .nombre("Carlos")
                .apellido("Ramírez")
                .email("carlos.ramirez@example.com")
                .documentoIdentidad(1234567890L)
                .telefono("3001234567")
                .idRol(2)
                .salarioBase(new BigDecimal("3500000"))
                .build();
    }

    @Test
    void shouldReturnTrueWhenUserExistsByEmail() {
        Mockito.when(usuarioRepository.existsByEmail(usuarioEjemplo.getEmail())).thenReturn(Mono.just(true));

        StepVerifier.create(usuarioUseCase.userExistsByEmail(usuarioEjemplo.getEmail()))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void shouldReturnFalseWhenUserDoesNotExistById() {
        Mockito.when(usuarioRepository.existsById(usuarioEjemplo.getIdUsuario())).thenReturn(Mono.just(false));

        StepVerifier.create(usuarioUseCase.userExistsById(usuarioEjemplo.getIdUsuario()))
                .expectNext(false)
                .verifyComplete();
    }

    @Test
    void shouldCreateUserWhenEmailIsNotTaken() {
        Mockito.when(usuarioRepository.existsByEmail(usuarioEjemplo.getEmail())).thenReturn(Mono.just(false));
        Mockito.when(usuarioRepository.save(usuarioEjemplo)).thenReturn(Mono.just(usuarioEjemplo));

        StepVerifier.create(usuarioUseCase.createUser(usuarioEjemplo))
                .expectNext(usuarioEjemplo)
                .verifyComplete();
    }

    @Test
    void shouldFailToCreateUserWhenEmailExists() {
        Mockito.when(usuarioRepository.existsByEmail(usuarioEjemplo.getEmail())).thenReturn(Mono.just(true));

        StepVerifier.create(usuarioUseCase.createUser(usuarioEjemplo))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("El email ya está en uso"))
                .verify();
    }

    @Test
    void shouldReturnUserById() {
        Mockito.when(usuarioRepository.findById(usuarioEjemplo.getIdUsuario())).thenReturn(Mono.just(usuarioEjemplo));

        StepVerifier.create(usuarioUseCase.getUserById(usuarioEjemplo.getIdUsuario()))
                .expectNext(usuarioEjemplo)
                .verifyComplete();
    }

    @Test
    void shouldReturnUserByEmail() {
        Mockito.when(usuarioRepository.findByEmail(usuarioEjemplo.getEmail())).thenReturn(Mono.just(usuarioEjemplo));

        StepVerifier.create(usuarioUseCase.getUserByEmail(usuarioEjemplo.getEmail()))
                .expectNext(usuarioEjemplo)
                .verifyComplete();
    }

    @Test
    void shouldDeleteUserById() {
        Mockito.when(usuarioRepository.deleteById(usuarioEjemplo.getIdUsuario())).thenReturn(Mono.empty());

        StepVerifier.create(usuarioUseCase.deleteUserById(usuarioEjemplo.getIdUsuario()))
                .verifyComplete();
    }

    @Test
    void shouldReturnAllUsers() {
        Usuario otroUsuario = usuarioEjemplo.toBuilder()
                .idUsuario(2)
                .email("laura.mendez@example.com")
                .nombre("Laura")
                .apellido("Méndez")
                .documentoIdentidad(9876543210L)
                .telefono("3019876543")
                .build();

        Mockito.when(usuarioRepository.findAll()).thenReturn(Flux.just(usuarioEjemplo, otroUsuario));

        StepVerifier.create(usuarioUseCase.getAllUser())
                .expectNext(usuarioEjemplo)
                .expectNext(otroUsuario)
                .verifyComplete();
    }

    @Test
    void shouldUpdateUserWhenExists() {
        Usuario actualizado = usuarioEjemplo.toBuilder()
                .nombre("Carlos Andrés")
                .salarioBase(new BigDecimal("4000000"))
                .build();

        Mockito.when(usuarioRepository.findById(usuarioEjemplo.getIdUsuario())).thenReturn(Mono.just(usuarioEjemplo));
        Mockito.when(usuarioRepository.save(actualizado)).thenReturn(Mono.just(actualizado));

        StepVerifier.create(usuarioUseCase.updateUser(usuarioEjemplo.getIdUsuario(), actualizado))
                .expectNext(actualizado)
                .verifyComplete();
    }

    @Test
    void shouldFailToUpdateWhenUserNotFound() {
        Usuario actualizado = usuarioEjemplo.toBuilder()
                .nombre("Carlos Andrés")
                .build();

        Mockito.when(usuarioRepository.findById(usuarioEjemplo.getIdUsuario())).thenReturn(Mono.empty());

        StepVerifier.create(usuarioUseCase.updateUser(usuarioEjemplo.getIdUsuario(), actualizado))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Usuario no encontrado"))
                .verify();
    }
}