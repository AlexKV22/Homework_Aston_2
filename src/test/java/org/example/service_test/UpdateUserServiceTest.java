package org.example.service_test;

import dao.DAOHibernateImpl;
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
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class UpdateUserServiceTest {

    @Mock
    DAOHibernateImpl dao;
    @InjectMocks
    UserServiceImpl service;

    private final LogCaptor logCaptor = LogCaptor.forClass(UserServiceImpl.class);

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
