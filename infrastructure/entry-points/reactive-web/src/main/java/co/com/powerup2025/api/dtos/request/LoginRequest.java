package co.com.powerup2025.api.dtos.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(
        name = "LoginRequest",
        description = "Record para login",
        requiredProperties = {"email", "password"}
)
public record LoginRequest(

        @Schema(description = "Email del usuario")
        @NotNull(message = "El email no puede ser nulo")
        @NotBlank(message = "El email no puede estar en blanco")
        @Email(message = "El email debe tener un formato válido")
        String email,

        @Schema(description = "Contraseña del usuario")
        @NotNull(message = "La contraseña no puede ser nula")
        @NotBlank(message = "La contraseña no puede estar en blanco")
        String password
) {}

