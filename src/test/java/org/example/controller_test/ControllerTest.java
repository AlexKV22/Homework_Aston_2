package org.example.controller_test;

import com.fasterxml.jackson.databind.ObjectMapper;
import myApp.App;
import myApp.assembler.UserModelAssembler;
import myApp.dto.dtoRequest.UserRequestDto;
import myApp.dto.dtoResponse.UserResponseDto;
import myApp.exception.NoSaveNewUserException;
import myApp.exception.NoUpdateUserException;
import myApp.exception.UserNotFoundException;
import myApp.model.User;
import myApp.rest.UserController;
import myApp.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.time.LocalDate;

import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@ContextConfiguration(classes = App.class)
class ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServiceImpl userService;

    @InjectMocks
    private UserController userController;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserModelAssembler userModelAssembler;


    @Test
    void createUserTest() throws Exception {
        UserRequestDto userRequestDto = new UserRequestDto("EGORKA", "23esd", 600);
        UserResponseDto userResponseDto = new UserResponseDto(1L, "EGORKA", "23esd", 600);
        User user = new User("EGORKA", "23esd", 600, Date.valueOf(LocalDate.now()));
        user.setId(1L);
        Mockito.when(userService.create(userRequestDto)).thenReturn(userResponseDto);

        mockMvc.perform(post("/user").content(objectMapper.writeValueAsBytes(userRequestDto)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.name").value("EGORKA"));
        Mockito.verify(userService, Mockito.times(1)).create(userRequestDto);
    }

    @Test
    void createInvalidUserTest() throws Exception {
        UserRequestDto userRequestDto = new UserRequestDto(null, "23esd", 600);
        mockMvc.perform(post("/user").content(objectMapper.writeValueAsBytes(userRequestDto)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        Mockito.verify(userService, never()).create(userRequestDto);
    }

    @Test
    void createUserWithExceptionFromServiceTest() throws Exception {
        UserRequestDto userRequestDto = new UserRequestDto("EGORKA", "23esd", 600);
        Mockito.when(userService.create(userRequestDto)).thenThrow(NoSaveNewUserException.class);
        mockMvc.perform(post("/user").content(objectMapper.writeValueAsBytes(userRequestDto)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
        Mockito.verify(userService, Mockito.times(1)).create(userRequestDto);
    }

    @Test
    void updateUserTest() throws Exception {
        UserRequestDto userRequestDto = new UserRequestDto("EGORKA", "23esd", 600);
        UserResponseDto userResponseDto = new UserResponseDto(1L, "MISHA", "23esd", 600);
        User user = new User("MISHA", "23esd", 600, Date.valueOf(LocalDate.now()));
        user.setId(1L);
        Mockito.when(userService.update(userRequestDto, 1L)).thenReturn(userResponseDto);

        mockMvc.perform(put("/user/{userId}", 1L).content(objectMapper.writeValueAsBytes(userRequestDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.name").value("MISHA"));
        Mockito.verify(userService, Mockito.times(1)).update(userRequestDto, 1L);
    }

    @Test
    void updateInvalidUserTest() throws Exception {
        UserRequestDto userRequestDto = new UserRequestDto(null, "23esd", 600);
        mockMvc.perform(put("/user/{userId}", 1L).content(objectMapper.writeValueAsBytes(userRequestDto)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        Mockito.verify(userService, never()).create(userRequestDto);
    }

    @Test
    void updateUserWithExceptionFromServiceTest() throws Exception {
        UserRequestDto userRequestDto = new UserRequestDto("EGORKA", "23esd", 600);
        Mockito.when(userService.update(userRequestDto, 1L)).thenThrow(NoUpdateUserException.class);
        mockMvc.perform(put("/user/{userId}", 1L).content(objectMapper.writeValueAsBytes(userRequestDto)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
        Mockito.verify(userService, Mockito.times(1)).update(userRequestDto, 1L);
    }

    @Test
    void deleteValidUserTest() throws Exception {
        mockMvc.perform(delete("/user/{userId}", 1L)).andExpect(status().isNoContent());
        Mockito.verify(userService, Mockito.times(1)).delete(1L);
    }

    @Test
    void deleteInvalidUserTest() throws Exception {
        Mockito.doThrow(UserNotFoundException.class).when(userService).delete(1L);
        mockMvc.perform(delete("/user/{userId}", 1L)).andExpect(status().isNotFound());
        Mockito.verify(userService, Mockito.times(1)).delete(1L);
    }

    @Test
    void getUserTest() throws Exception {
        User user = new User("MISHA", "23esd", 600, Date.valueOf(LocalDate.now()));
        user.setId(1L);
        UserResponseDto userResponseDto = new UserResponseDto(1L, "MISHA", "23esd", 600);
        Mockito.when(userService.read(1L)).thenReturn(user);
        EntityModel<UserResponseDto> model = EntityModel.of(userResponseDto,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserById(user.getId())).withRel("Чтение юзера"),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).createUser(null)).withRel("Создание юзера"),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).updateUser(user.getId(), null)).withRel("Обновление юзера"),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).deleteUser(user.getId())).withRel("Удаление юзера")
        );
        Mockito.when(userModelAssembler.toModel(user)).thenReturn(model);
        mockMvc.perform(get("/user/{userId}", 1L)).andExpect(status().isOk()).andExpect(jsonPath("$.name").value("MISHA"));
        Mockito.verify(userService, Mockito.times(1)).read(1L);
        Mockito.verify(userModelAssembler, Mockito.times(1)).toModel(user);
    }

    @Test
    void getInvalidUserTest() throws Exception {
        Mockito.doThrow(UserNotFoundException.class).when(userService).read(1L);
        mockMvc.perform(get("/user/{userId}", 1L)).andExpect(status().isNotFound());
        Mockito.verify(userService, Mockito.times(1)).read(1L);
    }
}
