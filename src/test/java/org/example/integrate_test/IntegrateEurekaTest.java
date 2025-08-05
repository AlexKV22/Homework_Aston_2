package org.example.integrate_test;

import myApp.UserApp;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = UserApp.class)
class IntegrateEurekaTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void registerEurekaTest() throws InterruptedException {
        Thread.sleep(14000);
        ResponseEntity<String> responseEureka = restTemplate.getForEntity("http://localhost:8761/eureka/apps", String.class);
        Assertions.assertEquals(HttpStatus.OK, responseEureka.getStatusCode());
        Assertions.assertTrue(responseEureka.getBody().toLowerCase().contains("userservice"));

    }

    @Test
    void loadConfigServerTest() {
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8888/userService/test", String.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertTrue(response.getBody().toLowerCase().contains("userservice-test"));
    }
}
