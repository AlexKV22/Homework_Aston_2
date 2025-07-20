package myApp.rest;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import myApp.assembler.UserModelAssembler;
import myApp.dto.dtoRequest.UserRequestDto;
import myApp.dto.dtoResponse.UserResponseDto;
import myApp.model.User;
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
import org.springframework.hateoas.EntityModel;


@OpenAPIDefinition(info = @Info(title = "API пользователя", description = "API управления пользователем"),
        servers = @Server(url = "http://localhost:8080"))
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final UserModelAssembler userModelAssembler;

    @Autowired
    public UserController(UserService userService, UserModelAssembler userModelAssembler) {
        this.userService = userService;
        this.userModelAssembler = userModelAssembler;
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
    public ResponseEntity<EntityModel<UserResponseDto>> createUser(
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
        User user = userService.create(userRequestDto);
        EntityModel<UserResponseDto> model = userModelAssembler.toModel(user);
        return ResponseEntity.ok(model);
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
    public ResponseEntity<EntityModel<UserResponseDto>> updateUser(
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
        User update = userService.update(userRequestDto, userId);
        EntityModel<UserResponseDto> model = userModelAssembler.toModel(update);
        return ResponseEntity.ok(model);
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
    public ResponseEntity<EntityModel<UserResponseDto>> getUserById(
        @Positive(message = "userId can be only positive") @Parameter(description = "Айди юзера для поиска", required = true) @PathVariable Long userId
    )
    {
        EntityModel<UserResponseDto> model = userModelAssembler.toModel(userService.read(userId));
        return ResponseEntity.ok(model);
    }
}
