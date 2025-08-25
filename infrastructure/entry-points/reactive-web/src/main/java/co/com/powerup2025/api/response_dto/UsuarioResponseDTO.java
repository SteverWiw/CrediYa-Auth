package co.com.powerup2025.api.response_dto;

import java.math.BigDecimal;

public record UsuarioResponseDTO(Integer idUsuario,
                                 String nombre,
                                 String apellido,
                                 String email,
                                 Long documentoIdentidad,
                                 String telefono,
                                 Integer idRol,
                                 BigDecimal salarioBase) {

}
