package org.example.service_test;

import myApp.dao.DAO;
import myApp.model.User;
import myApp.service.UserService;
import org.example.TestConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessResourceFailureException;


import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@SpringBootTest(classes = TestConfig.class)
class UserServiceTest {

    @Qualifier("mockDaoTest")
    @Autowired
    private DAO dao;
    @Qualifier("mockUserService")
    @Autowired
    private UserService userService;

    @Test
    void createValidUserTest() {
        Mockito.when(dao.save(any(User.class))).thenReturn(any(User.class));
        boolean b = userService.create("MASHA", "jgdghdhg", 45);
        Assertions.assertTrue(b);
    }

    @Test
    void createUserWithNull() {
        Mockito.when(dao.save(any(User.class))).thenThrow(NullPointerException.class);
        boolean b = userService.create("EGOR", "345SDD", 56);
        System.out.println(b);
        Assertions.assertFalse(b);
    }

    @Test
    void createInvalidUserTest() {
        Mockito.when(dao.save(any(User.class))).thenThrow(DataAccessResourceFailureException.class);
        boolean b = userService.create("EGOR", "345SDD", 56);
        Assertions.assertFalse(b);
    }


    @Test
    void readValidUserTest() {
        Mockito.when(dao.findById(anyLong())).thenReturn(Optional.of(new User()));
        User read = userService.read(anyLong());
        Assertions.assertNotNull(read);
    }

    @Test
    void readInvalidUserTest() {
        Mockito.when(dao.findById(anyLong())).thenReturn(Optional.ofNullable(null));
        User read = userService.read(anyLong());
        Assertions.assertNull(read);
    }
}
