package tech.engix.auth_service.mapper;


import org.mapstruct.Mapper;
import tech.engix.auth_service.dto.SignUpDto;
import tech.engix.auth_service.dto.UserResponseDTO;
import tech.engix.auth_service.dto.user.UserUpdateDTO;
import tech.engix.auth_service.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntityInsert(SignUpDto dto);

    UserResponseDTO toInsertDto(User dto);

    UserUpdateDTO toUpdate(User entity);
}
