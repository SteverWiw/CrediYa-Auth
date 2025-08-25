package co.com.powerup2025.api.request_dto;

import java.math.BigDecimal;

import javax.xml.transform.Source;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
public class UsuarioRequestDTO implements Source {

    // @Schema(description = "ID del usuario (solo para actualizaciones)", required
    // = false)
    private Integer idUsuario;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @Email(message = "El email debe tener formato válido")
    @NotBlank(message = "El email es obligatorio")
    private String email;

    @NotNull(message = "El documento de identidad es obligatorio")
    @Min(value = 1000000, message = "Documento inválido")
    private Long documentoIdentidad;

    @NotBlank(message = "El teléfono es obligatorio")
    private String telefono;

    @NotNull(message = "El rol es obligatorio")
    private Integer idRol;

    @NotNull(message = "El salario base es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El salario debe ser mayor a cero")
    @DecimalMax(value = "15000001", inclusive = false, message = "El salario debe ser menor o igual a 15000000")
    private BigDecimal salarioBase;

    @Override
    public void setSystemId(String systemId) {

    }

    @Override
    public String getSystemId() {
        return "";
    }

    @Override
    public boolean isEmpty() {
        return Source.super.isEmpty();
    }
}
