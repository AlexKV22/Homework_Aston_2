package myApp.rest;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
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

@OpenAPIDefinition(info = @Info(title = "API пользователя", description = "API управления пользователем"))
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserServiceDto userServiceDto;

    @Autowired
    public UserController(UserServiceDto userServiceDto) {
        this.userServiceDto = userServiceDto;
    }

    @Operation(summary = "Создание юзера", description = "Возвращает UserResponseDto с созданным юзером", responses = {
            @ApiResponse(responseCode = "200", description = "User create", content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
    })

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        UserResponseDto userResponseDto = userServiceDto.create(userRequestDto);
        return ResponseEntity.ok(userResponseDto);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserResponseDto> updateUser(@Valid @RequestBody UserRequestDto userRequestDto, @Positive(message = "userId can be only positive") @PathVariable Long userId) {
        UserResponseDto userResponseDto = userServiceDto.update(userRequestDto, userId);
        return ResponseEntity.ok(userResponseDto);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable @Positive(message = "userId can be only positive") Long userId) {
        userServiceDto.delete(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long userId) {
        UserResponseDto read = userServiceDto.read(userId);
        return ResponseEntity.ok(read);
    }
}
