package co.com.powerup2025.api.request_dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "UsuarioRequestDTO", description = "Usuario Request DTO", requiredProperties = { "nombre", "apellido",
        "email", "idRol", "salarioBase" })
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
public class UsuarioRequestDTO {

    @Schema(description = "ID del usuario (solo para actualizaciones)")
    private Integer idUsuario;

    @Schema(description = "Nombre del usuario")
    private String nombre;

    @Schema(description = "Apellido del usuario")
    private String apellido;

    @Schema(description = "Correo electrónico del usuario")
    private String email;

    @Schema(description = "Documento de identidad del usuario")
    private Long documentoIdentidad;

    @Schema(description = "Teléfono del usuario")
    private String telefono;

    @Schema(description = "ID del rol del usuario")
    private Integer idRol;

    @Schema(description = "Salario base del usuario")
    private BigDecimal salarioBase;

}
