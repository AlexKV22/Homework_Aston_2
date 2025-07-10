package myApp.dto.dtoResponse;

import jakarta.validation.constraints.NotNull;

public record UserResponseDto(
        @NotNull(message = "Id cannot be null")
        Long id,
        @NotNull(message = "Name is mandatory")
        String name,
        @NotNull(message = "email cannot be null")
        String email,
        @NotNull(message = "age cannot be null")
        Integer age) {}
