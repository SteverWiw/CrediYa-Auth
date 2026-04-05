package co.com.powerup2025.model.user;
import lombok.*;

import java.math.BigDecimal;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {
    private Integer idUsuario;
    private String nombre;
    private String apellido;
    private String email;
    private Long documentoIdentidad;
    private String telefono;
    private Long idRol;
    private BigDecimal salarioBase;
    private String password;
}
