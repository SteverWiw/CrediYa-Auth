package co.com.powerup2025.model.auth.gateways;

public interface TokenGeneratorPort {
    String generateToken(String subject, String role);
}