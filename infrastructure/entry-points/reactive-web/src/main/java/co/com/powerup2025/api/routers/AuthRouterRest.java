package co.com.powerup2025.api.routers;

import co.com.powerup2025.api.handlers.AuthHandler;
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

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration

public class AuthRouterRest {
    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/crediYa/api/v1/auth/login",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    method = RequestMethod.POST,
                    beanClass = AuthHandler.class,
                    beanMethod = "auth"
            )
    })
    public RouterFunction<ServerResponse> AuthRouterFunction(AuthHandler handler) {
        return RouterFunctions
                .nest(RequestPredicates.path("/crediYa/api/v1/auth"),
                        RouterFunctions
                                .route()
                                .POST("/login", handler::auth)
                                .build());
    }

}
