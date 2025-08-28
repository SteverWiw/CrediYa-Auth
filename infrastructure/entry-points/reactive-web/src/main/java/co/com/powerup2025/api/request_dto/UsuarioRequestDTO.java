package co.com.powerup2025.api.request_dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "UsuarioRequestDTO",
        description = "Usuario Request DTO",
        requiredProperties = { "nombre", "apellido", "email", "idRol", "salarioBase" }
)
public record UsuarioRequestDTO(

        @Schema(description = "ID del usuario (solo para actualizaciones)")
        Integer idUsuario,

        @Schema(description = "Nombre del usuario")
        String nombre,

        @Schema(description = "Apellido del usuario")
        String apellido,

        @Schema(description = "Correo electrónico del usuario")
        String email,

        @Schema(description = "Documento de identidad del usuario")
        Long documentoIdentidad,

        @Schema(description = "Teléfono del usuario")
        String telefono,

        @Schema(description = "ID del rol del usuario")
        Integer idRol,

        @Schema(description = "Salario base del usuario")
        BigDecimal salarioBase
) {}
