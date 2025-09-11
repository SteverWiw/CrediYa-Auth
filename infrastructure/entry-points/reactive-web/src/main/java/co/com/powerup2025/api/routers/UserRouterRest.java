package co.com.powerup2025.api.routers;

import co.com.powerup2025.api.dtos.request.UserRequest;
import co.com.powerup2025.api.dtos.response.ErrorResponse;
import co.com.powerup2025.api.dtos.response.UserResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import co.com.powerup2025.api.handlers.UsuarioHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Configuration
public class UserRouterRest {
    @Bean
    @RouterOperations({
            @RouterOperation(path = "/crediYa/api/v1/usuarios", method = RequestMethod.POST, beanClass = UsuarioHandler.class, beanMethod = "createUser", operation = @Operation(operationId = "createUser", tags = {
                    "Usuarios"}, summary = "Crear un nuevo usuario", description = "Crea un nuevo usuario en el sistema", requestBody = @RequestBody(required = true, description = "Datos del usuario a crear", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserRequest.class))), responses = {
                    @ApiResponse(responseCode = "201", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))),
                    @ApiResponse(responseCode = "400", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
            }))
    })

    public RouterFunction<ServerResponse> routerFunction(UsuarioHandler handler) {
        return RouterFunctions
                .nest(RequestPredicates.path("/crediYa"),
                        RouterFunctions
                                .route()
                                .POST("/api/v1/usuarios", handler::createUser)
                                .GET("/api/v1/usuarios", handler::getUser)
                                .build());
    }

}
