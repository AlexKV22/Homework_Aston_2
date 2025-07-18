package myApp.kafkaProducer;

import myApp.userMessageKafka.UserMessageKafka;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {
    private final KafkaTemplate<String, UserMessageKafka> kafkaTemplate;
    @Value("${app.kafka.topic}")
    private String topic;

    @Autowired
    public KafkaProducer(KafkaTemplate<String, UserMessageKafka> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(UserMessageKafka userMessageKafka) {
        this.kafkaTemplate.send(topic, userMessageKafka);
    }
}
