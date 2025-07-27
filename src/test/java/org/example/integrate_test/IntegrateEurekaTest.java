package org.example.integrate_test;

import myApp.UserApp;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = UserApp.class)
@TestPropertySource("classpath:bootstrap-test.properties")
class IntegrateEurekaTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void integrateEurekaTest() throws InterruptedException {
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8888/config-server/default", String.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Thread.sleep(20000);
        ResponseEntity<String> responseEureka = restTemplate.getForEntity("http://localhost:8761/eureka/apps", String.class);
        Assertions.assertEquals(HttpStatus.OK, responseEureka.getStatusCode());
        System.out.println(responseEureka.getBody());
        Assertions.assertTrue(responseEureka.getBody().toLowerCase().contains("userservice"));

    }
}
