package co.com.powerup2025.api.config;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import co.com.powerup2025.usecase.usuario.UsuarioUseCase;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WebFluxTest
class ConfigTest {

    @Autowired
    private WebTestClient webTestClient;

    @InjectMocks
    private UsuarioUseCase usuarioUseCase;

    @Test
    void shouldApplyCorsHeaders() {
        webTestClient.get()
                .uri("/api/some-endpoint")
                .header("Origin", "http://localhost:3000")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Access-Control-Allow-Origin", "http://localhost:3000")
                .expectHeader().valueEquals("Vary", "Origin");
    }

}