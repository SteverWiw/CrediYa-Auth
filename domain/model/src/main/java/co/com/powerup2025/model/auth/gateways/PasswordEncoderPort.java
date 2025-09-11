package co.com.powerup2025.model.auth.gateways;

public interface PasswordEncoderPort {

    boolean matches(String rawPassword, String hashedPassword);

    String encode(String rawPassword);

}