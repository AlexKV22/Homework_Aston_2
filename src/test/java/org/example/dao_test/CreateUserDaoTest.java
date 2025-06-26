package org.example.dao_test;

import dao.DAO;
import dao.DAOHibernateImpl;
import exception.CreateJDBCConnectionException;
import model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
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
import java.util.logging.Level;
import java.util.logging.Logger;


@Testcontainers
class CreateUserDaoTest extends TestContainerContext {

    private static final String url = postgreSQLContainer.getJdbcUrl();
    private static final String username = postgreSQLContainer.getUsername();
    private static final String password = postgreSQLContainer.getPassword();
    private static final Logger logger = Logger.getLogger(CreateUserDaoTest.class.getName());
    private DAO dao;

    @BeforeAll
    static void initDatabaseAndCreateSessionFactory() {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE SCHEMA if not exists aston");
            logger.info("Схема aston создана");
            statement.executeUpdate("CREATE TABLE if not exists aston.users (id SERIAL PRIMARY KEY, name VARCHAR, email VARCHAR, age INT, created_at DATE)");
            logger.info("Таблица users создана");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Не удалось соединиться с тестовым контейнером для создания схемы и таблицы");
            throw new CreateJDBCConnectionException("Не удалось соединиться с тестовым контейнером для создания схемы и таблицы", e);
        }
    }

    @AfterAll
    static void shutdownDatabase() {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DROP TABLE if exists users CASCADE ");
            logger.info("Таблица users удалена после тестов");
            statement.executeUpdate("DROP SCHEMA if exists aston CASCADE ");
            logger.info("Схема aston удалена после тестов");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Не удалось соединиться с тестовым контейнером для удаления схемы и таблицы после тестов");
            throw new CreateJDBCConnectionException("Не удалось соединиться с тестовым контейнером для удаления схемы и таблицы после тестов", e);
        }
    }

    @BeforeEach
    void setUp() {
        String sql = "INSERT INTO aston.users (name, email, age, created_at) VALUES (?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, "EGOR");
            preparedStatement.setString(2, "234@");
            preparedStatement.setInt(3, 345);
            preparedStatement.setDate(4, Date.valueOf(LocalDate.now()));
            preparedStatement.executeUpdate();
            logger.info("Тестовый user добавлен в таблицу users");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Не удалось соединиться с контейнером для заполнения данными таблицы перед каждым тестом");
            throw new CreateJDBCConnectionException("Не удалось соединиться с контейнером для заполнения данными таблицы перед каждым тестом", e);
        }
    }

    @AfterEach
    void tearDown() {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
             connection.createStatement().executeUpdate("TRUNCATE TABLE aston.users RESTART IDENTITY ");
            logger.info("Таблица users очищена");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Не удалось соединиться с контейнером для удаления данных таблицы после каждого теста");
            throw new CreateJDBCConnectionException("Не удалось соединиться с контейнером для удаления данных таблицы после каждого теста", e);
        }
    }

    private static SessionFactory getTestSessionFactory() {
        Configuration configuration = new Configuration();
        configuration.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
        configuration.setProperty("hibernate.connection.url", postgreSQLContainer.getJdbcUrl());
        configuration.setProperty("hibernate.connection.username", postgreSQLContainer.getUsername());
        configuration.setProperty("hibernate.connection.password", postgreSQLContainer.getPassword());
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        configuration.setProperty("hibernate.hbm2ddl.auto", "validate");
        configuration.setProperty("hibernate.show_sql", "true");
        configuration.setProperty("hibernate.default_schema", "aston");
        configuration.addAnnotatedClass(User.class);
        SessionFactory sessionFactory = configuration.buildSessionFactory();
        logger.info("SessionFactory в тестовом классе CreateUserDaoTest создана " + sessionFactory);
        return sessionFactory;
    }


    @Test
    void createValidUserTest() {
        SessionFactory sessionFactory = getTestSessionFactory();
        dao = new DAOHibernateImpl(sessionFactory);
        User user = new User ("Misha", "4552@", 45, Date.valueOf(LocalDate.now()));
        Assertions.assertTrue(dao.create(user));
        Session session = sessionFactory.openSession();
        User user1 = session.get(user.getClass(), user.getId());
        Assertions.assertEquals(user, user1);
        session.close();
        sessionFactory.close();
    }

    @Test
    void createUserWithNull() {
        SessionFactory sessionFactory = getTestSessionFactory();
        dao = new DAOHibernateImpl(sessionFactory);
        Assertions.assertThrows(NullPointerException.class, () -> dao.create(null));
        sessionFactory.close();
    }
}