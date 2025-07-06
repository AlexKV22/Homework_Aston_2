package org.example;

import myApp.dao.DAO;
import myApp.service.UserService;
import myApp.service.UserServiceImpl;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ComponentScan("myApp")
public class TestConfig {

    @Bean
    public DAO mockDaoTest() {
        return Mockito.mock(DAO.class);
    }

    @Bean
    public UserService mockUserService(@Qualifier("mockDaoTest") DAO mockDaoTest) {
        return new UserServiceImpl(mockDaoTest);
    }

}
