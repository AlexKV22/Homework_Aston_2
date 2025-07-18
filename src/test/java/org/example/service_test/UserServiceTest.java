package org.example.service_test;

import myApp.converter.UserMapper;
import myApp.dto.dtoRequest.UserRequestDto;
import myApp.dto.dtoResponse.UserResponseDto;
import myApp.exception.UserNotFoundException;
import myApp.kafkaProducer.KafkaProducer;
import myApp.model.User;
import myApp.repository.UserRepository;
import myApp.service.UserServiceImpl;
import myApp.userMessageKafka.UserMessageKafka;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private KafkaProducer kafkaProducer;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserMapper userMapper;

    @Test
    void createValidUserTest() {
        User user = new User("ENOT", "ssdsds", 56, Date.valueOf(LocalDate.now()));
        user.setId(1L);
        UserRequestDto userRequestDto = new UserRequestDto("ENOT", "ssdsds", 56);
        UserResponseDto userResponseDto = new UserResponseDto(1L, "ENOT", "ssdsds", 56);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.dtoToEntity(userRequestDto)).thenReturn(user);
        when(userMapper.entityToDto(user)).thenReturn(userResponseDto);
        UserResponseDto userResponseDtoUser = userService.create(userRequestDto);
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
        Mockito.verify(kafkaProducer, Mockito.times(1)).sendMessage(any(UserMessageKafka.class));
        Assertions.assertEquals(userResponseDto, userResponseDtoUser);
    }


    @Test
    void createInvalidUserTest() {
        User user = new User(null, "sddsd", 45, Date.valueOf(LocalDate.now()));
        UserRequestDto userRequestDto = new UserRequestDto(null, "ssdsds", 45);
        when(userMapper.dtoToEntity(userRequestDto)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenThrow(new NullPointerException("Cannot be null"));
        Assertions.assertThrows(NullPointerException.class, () -> userService.create(userRequestDto));
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
        Mockito.verify(kafkaProducer, never()).sendMessage(any(UserMessageKafka.class));
    }

    @Test
    void readValidUserTest() {
        User user = new User("ENOT", "ssdsds", 56, Date.valueOf(LocalDate.now()));
        user.setId(1L);
        UserResponseDto userResponseDto = new UserResponseDto(1L, "ENOT", "ssdsds", 56);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.entityToDto(user)).thenReturn(userResponseDto);
        UserResponseDto read = userService.read(1L);
        Assertions.assertEquals(userResponseDto, read);
        Mockito.verify(userRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    void readInvalidUserTest() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(null));
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.read(anyLong()));
        Mockito.verify(userRepository, Mockito.times(1)).findById(anyLong());
    }

    @Test
    void updateValidUserTest() {
        UserRequestDto userRequestDto = new UserRequestDto("MOLOKO", "rith", 100);
        UserResponseDto userResponseDto = new UserResponseDto(1L, "MOLOKO", "rith", 100);

        User userFromDto = new User("MOLOKO", "rith", 100, Date.valueOf(LocalDate.now()));

        User userFromDatabase = new User("ENOT", "ssdsds", 56, Date.valueOf(LocalDate.now()));
        userFromDatabase.setId(1L);

        User updatedUser = new User(userFromDto.getName(),
                userFromDto.getEmail(),
                userFromDto.getAge(),
                userFromDatabase.getCreated_at());
        updatedUser.setId(1L);

        Mockito.when(userMapper.dtoToEntity(userRequestDto)).thenReturn(userFromDto);
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(userFromDatabase));
        Mockito.when(userRepository.save(updatedUser)).thenReturn(updatedUser);
        Mockito.when(userMapper.entityToDto(updatedUser)).thenReturn(userResponseDto);
        UserResponseDto update = userService.update(userRequestDto, 1L);

        Assertions.assertEquals(userResponseDto.id(), update.id());
        Assertions.assertEquals(userResponseDto.name(), update.name());
        Assertions.assertEquals(userResponseDto.email(), update.email());
        Mockito.verify(userRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    void updateInvalidUserTest() {
        UserRequestDto userRequestDto = new UserRequestDto("MOLOKO", "rith", 100);
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(null));
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.update(userRequestDto, 1L));
        Mockito.verify(userRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    void deleteValidUserTest() {
        User user = new User("ENOT", "ssdsds", 56, Date.valueOf(LocalDate.now()));
        user.setId(1L);
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Assertions.assertDoesNotThrow(() -> userService.delete(1L));
        Mockito.verify(userRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(kafkaProducer, Mockito.times(1)).sendMessage(any(UserMessageKafka.class));
    }

    @Test
    void deleteInvalidUserTest() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(null));
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.delete(anyLong()));
        Mockito.verify(userRepository, Mockito.times(1)).findById(anyLong());
    }
}
