package org.example;

import myApp.repository.UserRepository;
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
}
