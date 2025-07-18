package myApp.rest;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import myApp.dto.dtoRequest.UserRequestDto;
import myApp.dto.dtoResponse.UserResponseDto;
import myApp.service.UserService;
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

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

/*------------------------------------------*/
    @Operation(summary = "Создание юзера",
               description = "Создает юзера, возвращает UserResponseDto с созданным юзером",
               responses = {
                        @ApiResponse(responseCode = "200", description = "Юзер успешно создан", content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
                        @ApiResponse(responseCode = "500", description = "Не удалось создать нового юзера"),
                        @ApiResponse(responseCode = "409", description = "Ошибка. Email уже занят"),
                        @ApiResponse(responseCode = "404", description = "Ресурс не найден")
               }
    )
    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(
                    @Valid
                    @io.swagger.v3.oas.annotations.parameters.RequestBody (
                            description = "Структура запроса к созданию юзера",
                            required = true,
                            content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = UserRequestDto.class),
                                examples = @ExampleObject(value = "{ \"name\": \"Igor\", \"email\": \"wer@mail\", \"age\": 44 }"))
                    )
                    @RequestBody UserRequestDto userRequestDto
    )
    {
        UserResponseDto userResponseDto = userService.create(userRequestDto);
        return ResponseEntity.ok(userResponseDto);
    }


/*------------------------------------------*/
    @Operation(summary = "Обновление юзера",
            description = "Обновляет юзера, возвращает UserResponseDto с обновленным юзером",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Юзер успешно обновлен", content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "500", description = "Не удалось обновить юзера"),
                    @ApiResponse(responseCode = "409", description = "Ошибка. Email уже занят"),
                    @ApiResponse(responseCode = "404", description = "Юзер в базе не найден")
            }
    )
    @PutMapping("/{userId}")
    public ResponseEntity<UserResponseDto> updateUser(
                    @Positive(message = "userId can be only positive") @Parameter(description = "Айди юзера для обновления", required = true) @PathVariable Long userId,
                    @io.swagger.v3.oas.annotations.parameters.RequestBody (
                            description = "Структура запроса к обновлению юзера",
                            required = true,
                            content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = UserRequestDto.class),
                                examples = @ExampleObject(value = "{ \"name\": \"Igor\", \"email\": \"wer@mail\", \"age\": 44 }"))
                    )
                    @Valid @RequestBody UserRequestDto userRequestDto
    )
    {
        UserResponseDto userResponseDto = userService.update(userRequestDto, userId);
        return ResponseEntity.ok(userResponseDto);
    }


/*------------------------------------------*/
    @Operation(summary = "Удаление юзера",
            description = "Удаляет юзера, ничего не возвращает",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Юзер успешно удален"),
                    @ApiResponse(responseCode = "500", description = "Не удалось удалить юзера"),
                    @ApiResponse(responseCode = "404", description = "Юзер в базе не найден")
            }
    )
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(
            @Positive(message = "userId can be only positive") @Parameter(description = "Айди юзера для удаления", required = true) @PathVariable Long userId
    )
    {
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }



/*------------------------------------------*/
    @Operation(summary = "Чтение юзера",
            description = "Читает юзера, возвращает UserResponseDto с найденным юзером",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Юзер успешно найден", content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "500", description = "Не удалось найти юзера, ошибка на сервере"),
                    @ApiResponse(responseCode = "404", description = "Юзер в базе не найден")
            }
    )
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUserById(
            @Positive(message = "userId can be only positive") @Parameter(description = "Айди юзера для поиска", required = true) @PathVariable Long userId
    )
    {
        UserResponseDto read = userService.read(userId);
        return ResponseEntity.ok(read);
    }
}
