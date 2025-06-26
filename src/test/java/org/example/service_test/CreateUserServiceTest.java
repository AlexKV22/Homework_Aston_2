package org.example.service_test;

import dao.DAOHibernateImpl;
import exception.NoSaveNewUserException;
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


import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class CreateUserServiceTest {

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
}
