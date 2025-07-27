package myApp.kafkaSender;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import myApp.kafkaProducer.KafkaProducer;
import myApp.model.User;
import myApp.userMessageKafka.UserMessageKafka;
import myApp.utils.StatusSendKafka;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class KafkaSender {

    private final KafkaProducer kafkaProducer;
    private final Logger logger = LoggerFactory.getLogger(KafkaSender.class);

    @Autowired
    public KafkaSender(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @CircuitBreaker(name="MyCircuitBreaker", fallbackMethod = "fallBack")
    public void sendingKafka(User user, StatusSendKafka status) {
        UserMessageKafka userMessageKafka = new UserMessageKafka();
        if (status.equals(StatusSendKafka.CREATE)) {
            userMessageKafka.setEmail(user.getEmail());
            userMessageKafka.setCreateOrDelete(StatusSendKafka.CREATE.getStatus());
            kafkaProducer.sendMessage(userMessageKafka);
        } else {
            userMessageKafka.setEmail(user.getEmail());
            userMessageKafka.setCreateOrDelete(StatusSendKafka.DELETE.getStatus());
            kafkaProducer.sendMessage(userMessageKafka);
        }
    }

    public void fallBack(User user, StatusSendKafka status, Throwable e) {
        logger.warn("Произошла ошибка отправки юзера {} в Kafka, операция {}, сообщение ошибки: {}", user, status, e.getMessage());
    }
}
