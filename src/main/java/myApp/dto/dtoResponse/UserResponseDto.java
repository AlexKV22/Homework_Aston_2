package myApp.dto.dtoResponse;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "DTO для ответа с информацией о пользователе")
public record UserResponseDto(
        @NotNull(message = "Id cannot be null")
        @Positive
        @Schema(description = "Айди юзера в ответе")
        Long id,
        @NotNull(message = "Name is mandatory")
        @NotBlank
        @Schema(description = "Имя юзера в ответе")
        String name,
        @NotNull(message = "email cannot be null")
        @NotBlank
        @Schema(description = "Email юзера в ответе")
        String email,
        @NotNull(message = "age cannot be null")
        @Positive
        @Schema(description = "Возраст юзера в ответе")
        Integer age) {}
