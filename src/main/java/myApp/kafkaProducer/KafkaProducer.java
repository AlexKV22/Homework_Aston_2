package myApp.kafkaProducer;

import myApp.userTempKafka.UserTempKafka;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {
    private final KafkaTemplate<String, UserTempKafka> kafkaTemplate;
    @Value("${app.kafka.topic}")
    private String topic;

    @Autowired
    public KafkaProducer(KafkaTemplate<String, UserTempKafka> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(UserTempKafka userTempKafka) {
        this.kafkaTemplate.send(topic, userTempKafka);
    }
}
