package co.com.powerup2025.api;

import co.com.powerup2025.api.handler.UsuarioHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;

import co.com.powerup2025.api.mapper.UsuarioMapper;
import co.com.powerup2025.model.usuario.Usuario;
import co.com.powerup2025.model.usuario.gateways.UsuarioRepository;
import co.com.powerup2025.usecase.usuario.UsuarioUseCase;

@ContextConfiguration(classes = {
        UsuarioRouterRest.class,
        UsuarioHandler.class,
        UsuarioMapper.class,
        UsuarioUseCase.class,
        Usuario.class
})
@WebFluxTest
class UsuarioRouterRestTest {

    private UsuarioRepository usuarioRepository;

    private UsuarioMapper usuarioMapper;

    private UsuarioUseCase usuarioUseCase;

    @Autowired
    private WebTestClient webTestClient;

    /*
     * @Test
     * void testCreateUserReturnsOk() {
     * UsuarioRequestDTO requestDTO = UsuarioRequestDTO.builder()
     * .nombre("Jhonathan")
     * .apellido("Ramírez")
     * .email("jhonathan@example.com")
     * .documentoIdentidad(12345678L)
     * .telefono("3001234567")
     * .idRol(1)
     * .salarioBase(new BigDecimal("5000000"))
     * .build();
     * 
     * 
     * webTestClient.post()
     * .uri("/crediYa/api/v1/usuarios")
     * .contentType(MediaType.APPLICATION_JSON)
     * .accept(MediaType.APPLICATION_JSON)
     * .bodyValue(requestDTO)
     * .exchange()
     * .expectStatus().isOk()
     * .expectBody(UsuarioRequestDTO.class)
     * .value(response -> {
     * Assertions.assertThat(response.getNombre()).isEqualTo("Jhonathan");
     * Assertions.assertThat(response.getEmail()).isEqualTo("jhonathan@example.com")
     * ;
     * });
     * }
     */

}