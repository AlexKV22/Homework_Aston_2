package org.example.service_test;

import dao.DAOHibernateImpl;
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
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
class ReadUserServiceTest {

    @Mock
    DAOHibernateImpl dao;
    @InjectMocks
    UserServiceImpl service;

    private final LogCaptor logCaptor = LogCaptor.forClass(UserServiceImpl.class);

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
}
