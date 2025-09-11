package co.com.powerup2025.api.mapper;


import co.com.powerup2025.api.dtos.request.UserRequest;
import co.com.powerup2025.api.dtos.response.UserResponse;
import co.com.powerup2025.model.user.User;
import org.mapstruct.Mapper;
import static org.mapstruct.factory.Mappers.getMapper;


@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = getMapper(UserMapper.class);

    User toEntity(UserRequest dto);

    UserResponse toDto(User entity);

}
