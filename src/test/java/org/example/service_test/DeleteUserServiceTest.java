package org.example.service_test;

import dao.DAOHibernateImpl;
import exception.NoDeleteUserException;
import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import service.UserServiceImpl;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
class DeleteUserServiceTest {

    @Mock
    DAOHibernateImpl dao;
    @InjectMocks
    UserServiceImpl service;

    private final LogCaptor logCaptor = LogCaptor.forClass(UserServiceImpl.class);

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

}
