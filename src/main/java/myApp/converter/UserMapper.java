package myApp.converter;

import myApp.dto.dtoRequest.UserRequestDto;
import myApp.dto.dtoResponse.UserResponseDto;
import myApp.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "age", source = "age")
    UserResponseDto entityToDto(User user);


    @Mapping(target = "name", source = "name")
    @Mapping(target = "email", source = "email")
    @Mapping(target ="age", source = "age")
    User dtoToEntity(UserRequestDto userRequestDto);
}
