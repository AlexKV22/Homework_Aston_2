package myApp.assembler;

import myApp.converter.UserMapper;
import myApp.dto.dtoResponse.UserResponseDto;
import myApp.model.User;
import myApp.rest.UserController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<User, EntityModel<UserResponseDto>> {

    private final UserMapper userMapper;

    @Autowired
    public UserModelAssembler(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public EntityModel<UserResponseDto> toModel(User entity) {
        UserResponseDto userResponseDto = userMapper.entityToDto(entity);
        return EntityModel.of(userResponseDto,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserById(entity.getId())).withRel("Чтение юзера"),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).deleteUser(entity.getId())).withRel("Удаление юзера")
                );
    }

}
