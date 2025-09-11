package co.com.powerup2025.api.adapters;

import co.com.powerup2025.api.jwt.JWTUtil;
import co.com.powerup2025.model.auth.gateways.TokenGeneratorPort;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JwtTokenAdapter implements TokenGeneratorPort {

    private final JWTUtil jwtUtil;

    public JwtTokenAdapter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }
    @PostConstruct
    public void logTokenAdapter() {
        System.out.println("TokenGeneratorPort activo: JwtTokenAdapter con JWTUtil");
    }

    @Override
    public String generateToken(String subject, String role) {
        return jwtUtil.createToken(subject, role);
    }
}