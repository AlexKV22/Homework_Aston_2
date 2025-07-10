package myApp.service.dto;

import myApp.converter.UserMapper;
import myApp.dto.dtoRequest.UserRequestDto;
import myApp.dto.dtoResponse.UserResponseDto;
import myApp.model.User;
import myApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserServiceDtoImpl implements UserServiceDto {
    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceDtoImpl(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @Override
    public UserResponseDto create(UserRequestDto userRequestDto) {
        User user = userMapper.dtoToEntity(userRequestDto);
        return userService.create(user);
    }

    @Override
    public UserResponseDto update(UserRequestDto userRequestDto, Long id) {
        User user = userMapper.dtoToEntity(userRequestDto);
        return userService.update(user, id);
    }

    @Override
    public void delete(Long id) {
        userService.delete(id);
    }

    @Override
    public UserResponseDto read(Long id) {
        return userService.read(id);
    }
}
