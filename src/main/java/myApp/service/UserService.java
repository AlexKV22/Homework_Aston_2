package myApp.service;

import myApp.dto.dtoRequest.UserRequestDto;
import myApp.dto.dtoResponse.UserResponseDto;
import myApp.model.User;

public interface UserService {
    User create(UserRequestDto userRequestDto);
    User update(UserRequestDto userRequestDto, Long id);
    void delete(Long id);
    User read(Long id);
}
