package org.example.service_test;

import dao.DAOHibernateImpl;
import exception.NoDeleteUserException;
import exception.NoSaveNewUserException;
import exception.NoUpdateUserException;
import model.User;
import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import service.UserServiceImpl;


import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    DAOHibernateImpl dao;
    @InjectMocks
    UserServiceImpl service;

    private final LogCaptor logCaptor = LogCaptor.forClass(UserServiceImpl.class);

    @Test
    void createValidUserTest() {
        Mockito.when(dao.create(any(User.class))).thenReturn(true);
        service.create("EGOR", "345SDD", 56);
        Mockito.verify(dao).create(any(User.class));
        boolean b = logCaptor.getLogs().stream().anyMatch(log -> log.contains("Создание user завершилось успешно"));
        Assertions.assertTrue(b, "Создание user завершилось успешно");
    }

    @Test
    void createUserWithNull() {
        Mockito.when(dao.create(any(User.class))).thenThrow(NullPointerException.class);
        service.create("EGOR", "345SDD", 56);
        Mockito.verify(dao).create(any(User.class));
        boolean b = logCaptor.getLogs().stream().anyMatch(log -> log.contains("Не удалось сохранить нового user, user null"));
        Assertions.assertTrue(b, "Не удалось сохранить нового user, user null");
    }

    @Test
    void createInvalidUserTest() {
        Mockito.when(dao.create(any(User.class))).thenThrow(NoSaveNewUserException.class);
        service.create("EGOR", "345SDD", 56);
        Mockito.verify(dao).create(any(User.class));
        boolean b = logCaptor.getLogs().stream().anyMatch(log -> log.contains("Не удалось сохранить нового user"));
        Assertions.assertTrue(b, "Не удалось сохранить нового user");
    }

    @Test
    void deleteValidUserTest() {
        Mockito.when(dao.delete(anyLong())).thenReturn(true);
        service.delete(anyLong());
        Mockito.verify(dao).delete(anyLong());
        boolean b = logCaptor.getLogs().stream().anyMatch(log -> log.contains("Удаление user завершилось успешно"));
        Assertions.assertTrue(b, "Удаление user завершилось успешно");
    }

    @Test
    void deleteInvalidUserWithNull() {
        Mockito.when(dao.delete(anyLong())).thenThrow(NoDeleteUserException.class);
        service.delete(anyLong());
        Mockito.verify(dao).delete(anyLong());
        boolean b = logCaptor.getLogs().stream().anyMatch(log -> log.contains("Не удалось удалить user"));
        Assertions.assertTrue(b, "Не удалось удалить user");
    }

    @Test
    void readValidUserTest() {
        Mockito.when(dao.read(anyLong())).thenReturn(Optional.of(new User()));
        User read = service.read(anyLong());
        Mockito.verify(dao).read(anyLong());
        Assertions.assertNotNull(read);
        boolean b = logCaptor.getLogs().stream().anyMatch(log -> log.contains("Чтение user завершилось успешно"));
        Assertions.assertTrue(b, "Чтение user завершилось успешно");
    }

    @Test
    void readInvalidUserTest() {
        Mockito.when(dao.read(anyLong())).thenReturn(Optional.ofNullable(null));
        User read = service.read(anyLong());
        Mockito.verify(dao).read(anyLong());
        Assertions.assertNull(read);
        boolean b = logCaptor.getLogs().stream().anyMatch(log -> log.contains("Не найден user c id:"));
        Assertions.assertTrue(b, "Не найден user c id:");
    }

    @Test
    void updateValidUserTest() {
        Mockito.when(dao.update(any(User.class))).thenReturn(true);
        service.update("EGOR", "345SDD", 56, new User());
        Mockito.verify(dao).update(any(User.class));
        boolean b = logCaptor.getLogs().stream().anyMatch(log -> log.contains("Обновление user завершилось успешно"));
        Assertions.assertTrue(b, "Обновление user завершилось успешно");
    }

    @Test
    void updateInvalidUserTest() {
        Mockito.when(dao.update(any(User.class))).thenThrow(NoUpdateUserException.class);
        service.update("EGOR", "345SDD", 56, new User());
        Mockito.verify(dao).update(any(User.class));
        boolean b = logCaptor.getLogs().stream().anyMatch(log -> log.contains("Не удалось обновить user"));
        Assertions.assertTrue(b, "Не удалось обновить user");
    }

}
