package myApp.service.dto;

import myApp.dto.dtoRequest.UserRequestDto;
import myApp.dto.dtoResponse.UserResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface UserServiceDto {
    UserResponseDto create(UserRequestDto userRequestDto);
    UserResponseDto update(UserRequestDto userRequestDto, Long id);
    void delete(Long id);
    UserResponseDto read(Long id);
}
