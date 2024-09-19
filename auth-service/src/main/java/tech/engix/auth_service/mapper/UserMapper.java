package tech.engix.auth_service.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import tech.engix.auth_service.dto.SignUpDto;
import tech.engix.auth_service.dto.UserResponseDTO;
import tech.engix.auth_service.dto.user.UserDto;
import tech.engix.auth_service.dto.user.UserUpdateDTO;
import tech.engix.auth_service.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDTO(User entity);

    User toEntity(UserDto dto);

    UserResponseDTO toInsertDto(User dto);

    User toEntityInsert(SignUpDto dto);

    UserUpdateDTO toUpdate(User entity);


}
