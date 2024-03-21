package top.kwseeker.msa.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import top.kwseeker.msa.kafka.domain.event.UserMessageEvent;
import top.kwseeker.msa.kafka.infrastructure.event.EventPublisher;

import javax.annotation.Resource;

@SpringBootApplication
public class KafkaExampleApplication implements CommandLineRunner {

    @Resource
    private EventPublisher eventPublisher;
    @Value("${kafka.topic.name}")
    private String topic;

    public static void main(String[] args) {
        SpringApplication.run(KafkaExampleApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        eventPublisher.publish(topic,
                new UserMessageEvent().buildEventMessage(
                        new UserMessageEvent.UserMessage("007", "Arvin", "Admin")));
    }
}
