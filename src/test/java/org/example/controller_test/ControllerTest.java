package org.example.controller_test;

import com.fasterxml.jackson.databind.ObjectMapper;
import myApp.App;
import myApp.dto.dtoRequest.UserRequestDto;
import myApp.dto.dtoResponse.UserResponseDto;
import myApp.rest.UserController;
import myApp.service.dto.UserServiceDto;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@ContextConfiguration(classes = App.class)
class ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServiceDto userServiceDto;

    @InjectMocks
    private UserController userController;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void createUserTest() throws Exception {
        UserRequestDto userRequestDto = new UserRequestDto("EGORKA", "23esd", 600);
        UserResponseDto userResponseDto = new UserResponseDto(1L, "EGORKA", "23esd", 600);
        Mockito.when(userServiceDto.create(userRequestDto)).thenReturn(userResponseDto);

        mockMvc.perform(post("/user/create").content(objectMapper.writeValueAsBytes(userRequestDto)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.name").value("EGORKA"));
        Mockito.verify(userServiceDto, Mockito.times(1)).create(userRequestDto);
    }

    @Test
    void updateUserTest() throws Exception {
        UserRequestDto userRequestDto = new UserRequestDto("EGORKA", "23esd", 600);
        UserResponseDto userResponseDto = new UserResponseDto(1L, "MISHA", "23esd", 600);
        Mockito.when(userServiceDto.update(userRequestDto, 1L)).thenReturn(userResponseDto);

        mockMvc.perform(put("/user/update/{userId}", 1L).content(objectMapper.writeValueAsBytes(userRequestDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.name").value("MISHA"));
        Mockito.verify(userServiceDto, Mockito.times(1)).update(userRequestDto, 1L);
    }

    @Test
    void deleteValidUserTest() throws Exception {
        mockMvc.perform(delete("/user/delete/{userId}", 1L)).andExpect(status().isNoContent());
        Mockito.verify(userServiceDto, Mockito.times(1)).delete(1L);
    }

    @Test
    void getUserTest() throws Exception {
        mockMvc.perform(get("/user/get/{userId}", 1L)).andExpect(status().isOk());
    }

}
