package myApp.repository.dto;

import myApp.dto.dtoResponse.UserResponseDto;
import myApp.model.User;

import java.util.Optional;

public interface UserRepositoryDto {
    UserResponseDto create(User user);
    UserResponseDto update(User user);
    Optional<User> findById(Long id);
    void deleteById(Long id);
    UserResponseDto getResponseDto(User user);
}
