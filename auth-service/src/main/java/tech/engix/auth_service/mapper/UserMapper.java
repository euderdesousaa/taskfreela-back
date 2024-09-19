package tech.engix.auth_service.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import tech.engix.auth_service.dto.SignUpDto;
import tech.engix.auth_service.dto.UserResponseDTO;
import tech.engix.auth_service.dto.user.UserDto;
import tech.engix.auth_service.dto.user.UserUpdateDTO;
import tech.engix.auth_service.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)  // Ignora a propriedade 'id'
    @Mapping(target = "authProvider", ignore = true)
    User toEntityInsert(SignUpDto dto);

    @Mapping(source = "dto.name", target = "name")
    @Mapping(source = "dto.email", target = "email")
    UserResponseDTO toInsertDto(User dto);

    @Mapping(source = "entity.name", target = "name")
    @Mapping(source = "entity.email", target = "email")
    UserUpdateDTO toUpdate(User entity);
}
