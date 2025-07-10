package myApp.dto.dtoRequest;

import jakarta.validation.constraints.NotNull;


public record UserRequestDto(
        @NotNull(message = "Name is null")
        String name,
        @NotNull(message = "Email is null")
        String email,
        @NotNull(message = "Age is null")
        Integer age) {}

