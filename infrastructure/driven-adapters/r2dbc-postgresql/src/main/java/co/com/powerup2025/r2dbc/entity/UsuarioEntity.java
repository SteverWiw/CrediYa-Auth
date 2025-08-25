package co.com.powerup2025.r2dbc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("Usuario")
public class UsuarioEntity {

    @Id
    @Column("id_usuario")
    private Integer idUsuario;

    @Column("nombre")
    private String nombre;

    @Column("apellido")
    private String apellido;

    @Column("email")
    private String email;

    @Column("documento_identidad")
    private Long documentoIdentidad;

    @Column("telefono")
    private String telefono;

    @Column("id_rol")
    private Integer idRol;

    @Column("salario_base")
    private BigDecimal salarioBase;
}