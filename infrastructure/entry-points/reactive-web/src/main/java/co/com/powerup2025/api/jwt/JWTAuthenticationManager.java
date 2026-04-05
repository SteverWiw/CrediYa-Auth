package co.com.powerup2025.api.jwt;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;

import co.com.powerup2025.model.user.gateways.IUserUseCase;
import reactor.core.publisher.Mono;

@Component
public class JWTAuthenticationManager implements ReactiveAuthenticationManager {

    private final JWTUtil jwtUtil;
    private final IUserUseCase userUseCase;

    public JWTAuthenticationManager(JWTUtil jwtUtil, IUserUseCase userUseCase) {
        this.jwtUtil = jwtUtil;
        this.userUseCase = userUseCase;
    }

    @Bean
    public ServerAuthenticationConverter authenticationConverter() {
        return exchange -> {
            String token = exchange.getRequest().getHeaders().getFirst("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                return Mono.just(SecurityContextHolder.getContext().getAuthentication());
            }
            return Mono.empty();
        };
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = authentication.getCredentials().toString();
        String username = jwtUtil.getUsername(token);

        return userUseCase.findUserByEmail(username)
                .flatMap(user -> {
                    if (!jwtUtil.isTokenValid(token, user.getEmail())) {
                        return Mono.error(new BadCredentialsException("Token inválido o expirado"));
                    }

                    List<String> roles = jwtUtil.getRoles(token);
                    List<GrantedAuthority> authorities = roles.stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

                    return Mono.just(new UsernamePasswordAuthenticationToken(username, token, authorities));
                });
    }
}


