package co.com.powerup2025.api.response_dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "UsuarioResponseDTO", description = "Usuario Response DTO")
public record UsuarioResponseDTO(
        @Schema(description = "ID del usuario", example = "1") Integer idUsuario,
        @Schema(description = "Nombre del usuario", example = "Juan") String nombre,
        @Schema(description = "Apellido del usuario", example = "Pérez") String apellido,
        @Schema(description = "Correo electrónico del usuario", example = "juan.perez@example.com") String email,
        @Schema(description = "Documento de identidad del usuario", example = "123456789") Long documentoIdentidad,
        @Schema(description = "Teléfono del usuario", example = "+573001234567") String telefono,
        @Schema(description = "ID del rol del usuario", example = "2") Integer idRol,
        @Schema(description = "Salario base del usuario", example = "2500.00") BigDecimal salarioBase) {

}
