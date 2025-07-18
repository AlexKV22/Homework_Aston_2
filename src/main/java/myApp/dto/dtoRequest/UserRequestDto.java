package myApp.dto.dtoRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "ДТО для создания и обновления юзера")
public record UserRequestDto(
        @NotNull(message = "Name is null")
        @NotBlank(message = "Name cannot be empty")
        @Schema(description = "Имя юзера в запросе")
        String name,
        @NotNull(message = "Email is null")
        @NotBlank(message = "Email cannot be empty")
        @Schema(description = "Email юзера в запросе")
        String email,
        @NotNull(message = "Age is null")
        @Positive(message = "Age can be only positive")
        @Schema(description = "Возраст юзера в запросе")
        Integer age) {}

