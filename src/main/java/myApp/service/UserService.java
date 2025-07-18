package myApp.service;

import myApp.dto.dtoRequest.UserRequestDto;
import myApp.dto.dtoResponse.UserResponseDto;
import myApp.model.User;

public interface UserService {
    UserResponseDto create(UserRequestDto userRequestDto);
    UserResponseDto update(UserRequestDto userRequestDto, Long id);
    void delete(Long id);
    UserResponseDto read(Long id);
}
