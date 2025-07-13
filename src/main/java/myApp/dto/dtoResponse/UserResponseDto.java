package myApp.dto.dtoResponse;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UserResponseDto(
        @NotNull(message = "Id cannot be null")
        @Positive
        Long id,
        @NotNull(message = "Name is mandatory")
        @NotBlank
        String name,
        @NotNull(message = "email cannot be null")
        @NotBlank
        String email,
        @NotNull(message = "age cannot be null")
        @Positive
        Integer age) {}
