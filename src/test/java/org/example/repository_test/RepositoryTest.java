package org.example.repository_test;

import myApp.UserApp;
import myApp.repository.UserRepository;
import myApp.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlScriptsTestExecutionListener;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.transaction.TransactionSystemException;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;


@DataJpaTest
@ContextConfiguration(classes = UserApp.class)
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        SqlScriptsTestExecutionListener.class
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS, value = "/schemaAndTableDrop.sql")
@ActiveProfiles("test")
@TestPropertySource("classpath:bootstrap-test.properties")
class RepositoryTest {

    @Autowired
    private UserRepository bean;

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, value = "/insertTable.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, value = "/truncateTable.sql")
    void createValidUserTest() {
        User user = new User ("Misha", "4552@", 45, Date.valueOf(LocalDate.now()));
        User saveUser = bean.save(user);
        Optional<User> byId = bean.findById(saveUser.getId());
        Assertions.assertEquals(saveUser, byId.get());
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, value = "/insertTable.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, value = "/truncateTable.sql")
    void readValidUserTest() {
        Optional<User> read = bean.findById(1L);
        Assertions.assertTrue(read.isPresent());
        Assertions.assertEquals("EGOR", read.get().getName());
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, value = "/insertTable.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, value = "/truncateTable.sql")
    void createUserWithNullTest() {
        Assertions.assertThrows(DataAccessException.class, () -> bean.save(null));
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, value = "/insertTable.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, value = "/truncateTable.sql")
    void deleteInvalidUserTest() {
        Assertions.assertDoesNotThrow(() -> bean.deleteById(2L));
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, value = "/insertTable.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, value = "/truncateTable.sql")
    void deleteValidUserTest() {
        bean.deleteById(1L);
        Optional<User> byId = bean.findById(1L);
        Assertions.assertFalse(byId.isPresent());
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, value = "/insertTable.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, value = "/truncateTable.sql")
    void readInvalidUserTest() {
        Optional<User> read = bean.findById(2L);
        Assertions.assertFalse(read.isPresent());
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, value = "/insertTable.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, value = "/truncateTable.sql")
    void updateValidUserValidTest() {
        User user = bean.findById(1L).get();
        user.setName("Marta");
        user.setEmail("marta@gmail.com");
        user.setAge(345);
        user.setCreated_at(Date.valueOf(LocalDate.now()));
        User saveUser = bean.save(user);
        Assertions.assertEquals(user.getId(), saveUser.getId());
        user = bean.findById(1L).get();
        Assertions.assertEquals("Marta", user.getName());
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, value = "/insertTable.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, value = "/truncateTable.sql")
    void updateValidUserInvalidTest() {
        User user = bean.findById(1L).get();
        user.setName(null);
        user.setEmail("marta@gmail.com");
        user.setAge(345);
        user.setCreated_at(Date.valueOf(LocalDate.now()));
        Assertions.assertThrows(TransactionSystemException.class, () -> bean.save(user));
    }
}