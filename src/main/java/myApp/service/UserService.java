package myApp.service;

import myApp.dto.dtoResponse.UserResponseDto;
import myApp.model.User;

public interface UserService {
    UserResponseDto create(User user);
    UserResponseDto update(User user, Long id);
    void delete(Long id);
    UserResponseDto read(Long id);
}
