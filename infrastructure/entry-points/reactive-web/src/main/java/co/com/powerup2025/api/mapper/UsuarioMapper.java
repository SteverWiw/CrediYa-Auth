package co.com.powerup2025.api.mapper;


import co.com.powerup2025.api.request_dto.UsuarioRequestDTO;
import co.com.powerup2025.api.response_dto.UsuarioResponseDTO;
import co.com.powerup2025.model.usuario.Usuario;
import org.mapstruct.Mapper;
import static org.mapstruct.factory.Mappers.getMapper;


@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    UsuarioMapper INSTANCE = getMapper(UsuarioMapper.class);

    Usuario toEntity(UsuarioRequestDTO dto);

    UsuarioResponseDTO toDto(Usuario entity);

}
