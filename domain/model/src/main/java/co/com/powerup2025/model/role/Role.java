package co.com.powerup2025.model.role;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Role {
    private Long idRole;
    private String nombre;
    private String descripcion;
}
