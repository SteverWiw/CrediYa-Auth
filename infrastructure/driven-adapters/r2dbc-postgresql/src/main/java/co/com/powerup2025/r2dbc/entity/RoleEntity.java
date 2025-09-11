package co.com.powerup2025.r2dbc.entity;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "rol",schema = "auth")
public class RoleEntity {

    @Column("idrol")
    private Long idRole;

    @Column("nombre")
    private String nombre;

    @Column
    private String descripcion;
}
