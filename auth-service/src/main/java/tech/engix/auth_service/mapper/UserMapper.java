package tech.engix.auth_service.mapper;


import org.mapstruct.*;
import tech.engix.auth_service.dto.SignUpDto;
import tech.engix.auth_service.dto.responses.UserClientResponse;
import tech.engix.auth_service.dto.responses.UserResponseDTO;
import tech.engix.auth_service.dto.user.UserUpdateDTO;
import tech.engix.auth_service.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntityInsert(SignUpDto dto);

    UserResponseDTO toInsertDto(User dto);

    UserUpdateDTO toUpdate(User entity);

    UserClientResponse toClientResponse(User user);

    @Mapping(target = "name", source = "dto.name")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toEntityUpdate(UserUpdateDTO dto, @MappingTarget User user);
}
