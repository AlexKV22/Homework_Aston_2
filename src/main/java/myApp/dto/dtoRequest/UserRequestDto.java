package myApp.dto.dtoRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;


public record UserRequestDto(
        @NotNull(message = "Name is null")
        @NotBlank(message = "Name cannot be empty")
        String name,
        @NotNull(message = "Email is null")
        @NotBlank(message = "Email cannot be empty")
        String email,
        @NotNull(message = "Age is null")
        @Positive(message = "Age can be only positive")
        Integer age) {}

