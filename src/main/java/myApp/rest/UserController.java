package myApp.rest;

import jakarta.validation.Valid;
import myApp.dto.dtoRequest.UserRequestDto;
import myApp.dto.dtoResponse.UserResponseDto;
import myApp.service.dto.UserServiceDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserServiceDto userServiceDto;

    @Autowired
    public UserController(UserServiceDto userServiceDto) {
        this.userServiceDto = userServiceDto;
    }

    @PostMapping("/create")
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        UserResponseDto userResponseDto = userServiceDto.create(userRequestDto);
        return ResponseEntity.ok(userResponseDto);
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<UserResponseDto> updateUser(@Valid @RequestBody UserRequestDto userRequestDto, @PathVariable Long userId) {
        UserResponseDto userResponseDto = userServiceDto.update(userRequestDto, userId);
        return ResponseEntity.ok(userResponseDto);
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userServiceDto.delete(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/get/{userId}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long userId) {
        UserResponseDto read = userServiceDto.read(userId);
        return ResponseEntity.ok(read);
    }
}
