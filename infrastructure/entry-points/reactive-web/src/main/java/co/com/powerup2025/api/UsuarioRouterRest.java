package co.com.powerup2025.api;

import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class UsuarioRouterRest {
        @Bean
        @RouterOperations({
                        @RouterOperation(path = "/crediYa/api/v1/usuarios", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST, beanClass = UsuarioHandler.class, beanMethod = "listenPOSTUseCase")
        })

        public RouterFunction<ServerResponse> routerFunction(UsuarioHandler handler) {
                return RouterFunctions
                                .nest(RequestPredicates.path("/crediYa"),
                                                RouterFunctions
                                                                .route()
                                                                .POST("/api/v1/usuarios", handler::createUser)
                                                                .build());
        }

}
