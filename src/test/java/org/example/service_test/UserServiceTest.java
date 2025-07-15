package org.example.service_test;

import myApp.converter.UserMapper;
import myApp.dto.dtoRequest.UserRequestDto;
import myApp.dto.dtoResponse.UserResponseDto;
import myApp.exception.UserNotFoundException;
import myApp.model.User;
import myApp.repository.dto.UserRepositoryDtoImpl;
import myApp.service.UserServiceImpl;
import myApp.userTempKafka.UserTempKafka;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;


import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepositoryDtoImpl userRepositoryDto;

    @Mock
    private KafkaTemplate<String, UserTempKafka> kafkaTemplate;

    @InjectMocks
    private UserServiceImpl userService;

    UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    void createValidUserTest() {
        User user = new User("ENOT", "ssdsds", 56, Date.valueOf(LocalDate.now()));
        user.setId(1L);
        UserResponseDto userResponseDto = userMapper.entityToDto(user);
        when(userRepositoryDto.create(user)).thenReturn(userResponseDto);
        UserResponseDto userResponseDtoUser = userService.create(user);
        Mockito.verify(userRepositoryDto, Mockito.times(1)).create(user);
        Assertions.assertEquals(userResponseDto, userResponseDtoUser);
    }


    @Test
    void createInvalidUserTest() {
        User user = new User(null, "sddsd", 45, Date.valueOf(LocalDate.now()));
        when(userRepositoryDto.create(any(User.class))).thenThrow(new NullPointerException("Cannot be null"));
        Assertions.assertThrows(NullPointerException.class, () -> userService.create(user));
        Mockito.verify(userRepositoryDto, Mockito.times(1)).create(user);
    }

    @Test
    void readValidUserTest() {
        User user = new User("ENOT", "ssdsds", 56, Date.valueOf(LocalDate.now()));
        user.setId(1L);
        UserResponseDto userResponseDto = userMapper.entityToDto(user);
        when(userRepositoryDto.findById(1L)).thenReturn(Optional.of(user));
        when(userRepositoryDto.getResponseDto(user)).thenReturn(userResponseDto);
        UserResponseDto read = userService.read(1L);
        Assertions.assertEquals(userResponseDto, read);
        Mockito.verify(userRepositoryDto, Mockito.times(1)).findById(1L);
    }

    @Test
    void readInvalidUserTest() {
        when(userRepositoryDto.findById(anyLong())).thenReturn(Optional.ofNullable(null));
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.read(anyLong()));
        Mockito.verify(userRepositoryDto, Mockito.times(1)).findById(anyLong());
    }

    @Test
    void updateValidUserTest() {
        UserRequestDto userRequestDto = new UserRequestDto("MOLOKO", "rith", 100);
        User userFromRequest = userMapper.dtoToEntity(userRequestDto);

        User user = new User("ENOT", "ssdsds", 56, Date.valueOf(LocalDate.now()));
        user.setId(1L);

        User updatedUser = new User(userRequestDto.name(),
                userRequestDto.email(),
                userRequestDto.age(),
                user.getCreated_at());
        updatedUser.setId(1L);

        UserResponseDto userResponseDto = userMapper.entityToDto(updatedUser);
        Mockito.when(userRepositoryDto.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(userRepositoryDto.update(userFromRequest)).thenReturn(userResponseDto);
        UserResponseDto update = userService.update(userFromRequest, 1L);
        Assertions.assertEquals(userResponseDto.id(), update.id());
        Assertions.assertEquals(userResponseDto.name(), update.name());
        Assertions.assertEquals(userResponseDto.email(), update.email());
        Mockito.verify(userRepositoryDto, Mockito.times(1)).findById(1L);
    }

    @Test
    void updateInvalidUserTest() {
        UserRequestDto userRequestDto = new UserRequestDto("MOLOKO", "rith", 100);
        User userFromRequest = userMapper.dtoToEntity(userRequestDto);
        Mockito.when(userRepositoryDto.findById(1L)).thenReturn(Optional.ofNullable(null));
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.update(userFromRequest, 1L));
        Mockito.verify(userRepositoryDto, Mockito.times(1)).findById(1L);
    }

    @Test
    void deleteValidUserTest() {
        User user = new User("ENOT", "ssdsds", 56, Date.valueOf(LocalDate.now()));
        user.setId(1L);
        Mockito.when(userRepositoryDto.findById(1L)).thenReturn(Optional.of(user));
        Assertions.assertDoesNotThrow(() -> userService.delete(1L));
        Mockito.verify(userRepositoryDto, Mockito.times(1)).findById(1L);
    }

    @Test
    void deleteInvalidUserTest() {
        when(userRepositoryDto.findById(anyLong())).thenReturn(Optional.ofNullable(null));
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.delete(anyLong()));
        Mockito.verify(userRepositoryDto, Mockito.times(1)).findById(anyLong());
    }
}
