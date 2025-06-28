package org.example.dao_test;

import dao.DAO;
import dao.DAOHibernateImpl;
import exception.CreateJDBCConnectionException;
import exception.NoDeleteUserException;
import jakarta.persistence.PersistenceException;
import model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;


@Testcontainers
class DaoTest extends TestContainerContext {

    private static final String URL = postgreSQLContainer.getJdbcUrl();
    private static final String USERNAME = postgreSQLContainer.getUsername();
    private static final String PASSWORD = postgreSQLContainer.getPassword();
    private static final Logger LOGGER = Logger.getLogger(DaoTest.class.getName());
    private final SessionFactory sessionFactory = TestContainerContext.getTestSessionFactory();
    private DAO dao = new DAOHibernateImpl(sessionFactory);

    @BeforeAll
    static void initDatabaseAndCreateSessionFactory() {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE SCHEMA if not exists aston");
            LOGGER.info("Схема aston создана");
            statement.executeUpdate("CREATE TABLE if not exists aston.users (id SERIAL PRIMARY KEY, name VARCHAR, email VARCHAR, age INT, created_at DATE)");
            LOGGER.info("Таблица users создана");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Не удалось соединиться с тестовым контейнером для создания схемы и таблицы");
            throw new CreateJDBCConnectionException("Не удалось соединиться с тестовым контейнером для создания схемы и таблицы", e);
        }
    }

    @AfterAll
    static void shutdownDatabase() {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DROP TABLE if exists users CASCADE ");
            LOGGER.info("Таблица users удалена после тестов");
            statement.executeUpdate("DROP SCHEMA if exists aston CASCADE ");
            LOGGER.info("Схема aston удалена после тестов");
            shutdownSessionFactory();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Не удалось соединиться с тестовым контейнером для удаления схемы и таблицы после тестов");
            throw new CreateJDBCConnectionException("Не удалось соединиться с тестовым контейнером для удаления схемы и таблицы после тестов", e);
        }
    }

    @BeforeEach
    void setUp() {
        String sql = "INSERT INTO aston.users (name, email, age, created_at) VALUES (?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, "EGOR");
            preparedStatement.setString(2, "234@");
            preparedStatement.setInt(3, 345);
            preparedStatement.setDate(4, Date.valueOf(LocalDate.now()));
            preparedStatement.executeUpdate();
            LOGGER.info("Тестовый user добавлен в таблицу users");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Не удалось соединиться с контейнером для заполнения данными таблицы перед каждым тестом");
            throw new CreateJDBCConnectionException("Не удалось соединиться с контейнером для заполнения данными таблицы перед каждым тестом", e);
        }
    }

    @AfterEach
    void tearDown() {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
             connection.createStatement().executeUpdate("TRUNCATE TABLE aston.users RESTART IDENTITY ");
            LOGGER.info("Таблица users очищена");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Не удалось соединиться с контейнером для удаления данных таблицы после каждого теста");
            throw new CreateJDBCConnectionException("Не удалось соединиться с контейнером для удаления данных таблицы после каждого теста", e);
        }
    }


    @Test
    void createValidUserTest() {
        User user = new User ("Misha", "4552@", 45, Date.valueOf(LocalDate.now()));
        Assertions.assertTrue(dao.create(user));
        Session session = sessionFactory.openSession();
        User user1 = session.get(user.getClass(), user.getId());
        Assertions.assertEquals(user, user1);
        session.close();
    }

    @Test
    void createUserWithNull() {
        Assertions.assertThrows(NullPointerException.class, () -> dao.create(null));
    }

    @Test
    void deleteInvalidUser() {
        Assertions.assertThrows(NoDeleteUserException.class, () -> dao.delete(2L));
    }

    @Test
    void deleteValidUser() {
        Assertions.assertTrue(dao.delete(1L));
    }

    @Test
    void readValidUserTest() {
        Optional<User> read = dao.read(1L);
        Assertions.assertTrue(read.isPresent());
        Assertions.assertEquals("EGOR", read.get().getName());
    }

    @Test
    void readInvalidUserTest() {
        Optional<User> read = dao.read(2L);
        Assertions.assertFalse(read.isPresent());
    }

    @Test
    void updateValidUserValid() {
        Session session = sessionFactory.openSession();
        User user = session.get(User.class, 1);
        user.setName("Marta");
        user.setEmail("marta@gmail.com");
        user.setAge(345);
        user.setCreated_at(Date.valueOf(LocalDate.now()));
        Assertions.assertTrue(dao.update(user));
        user = session.get(User.class, 1);
        Assertions.assertEquals("Marta", user.getName());
        session.close();
    }

    @Test
    void updateValidUserInvalid() {
        Session session = sessionFactory.openSession();
        User user = session.get(User.class, 1);
        user.setName(null);
        user.setEmail("marta@gmail.com");
        user.setAge(345);
        user.setCreated_at(Date.valueOf(LocalDate.now()));
        Assertions.assertThrows(PersistenceException.class, () -> dao.update(user));
        session.close();
    }

    @Test
    void updateInvalidUser() {
        Session session = sessionFactory.openSession();
        User user = session.get(User.class, 2);
        Assertions.assertNull(user);
        session.close();
    }
}