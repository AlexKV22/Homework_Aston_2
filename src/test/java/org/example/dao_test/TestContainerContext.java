package org.example.dao_test;

import model.User;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.util.logging.Level;
import java.util.logging.Logger;

public class TestContainerContext {
    private static final Logger LOGGER = Logger.getLogger(TestContainerContext.class.getName());
    private static SessionFactory sessionFactory;

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("testname")
            .withPassword("testpassword");

    protected static SessionFactory getTestSessionFactory() {
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
        sessionFactory = configuration.buildSessionFactory();
        LOGGER.info("SessionFactory в тестовом классе создана " + sessionFactory);
        return sessionFactory;
    }

    protected static void shutdownSessionFactory() {
        try {
            sessionFactory.close();
        } catch (HibernateException e) {
            LOGGER.log(Level.SEVERE,"Ошибка в закрытии SessionFactory в тестовом классе. " + e.getMessage());
        }
        LOGGER.info("SessionFactory в тестовом классе успешно закрыта.");
    }

    static {
        postgreSQLContainer.start();
    }
}
