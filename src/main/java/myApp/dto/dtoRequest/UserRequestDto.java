package myApp.dto.dtoRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "ДТО для создания и обновления юзера")
public record UserRequestDto(
        @NotNull(message = "Имя не может быть null")
        @NotBlank(message = "Имя не может быть пустым")
        @Schema(description = "Имя юзера в запросе")
        String name,
        @NotNull(message = "Email не может быть null")
        @NotBlank(message = "Email не может быть пустым")
        @Schema(description = "Email юзера в запросе")
        String email,
        @NotNull(message = "Возраст не может быть null")
        @Positive(message = "Число должно быть только положительным")
        @Schema(description = "Возраст юзера в запросе")
        Integer age) {}

