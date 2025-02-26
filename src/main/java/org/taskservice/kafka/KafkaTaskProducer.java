package org.taskservice.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.taskservice.dto.TaskDto;

@Slf4j
@Component
public class KafkaTaskProducer {
    private final KafkaTemplate<String, TaskDto> kafkaTaskTemplate;

    public KafkaTaskProducer(KafkaTemplate<String, TaskDto> kafkaTaskTemplate) {
        this.kafkaTaskTemplate = kafkaTaskTemplate;
    }

    public void send(TaskDto taskDto) {
        try {
            kafkaTaskTemplate.sendDefault(taskDto);
            kafkaTaskTemplate.flush();
        } catch (Exception e) {
            log.error("Kafka producer error sending message: {} {}", taskDto, e.getMessage());
        }
    }

    public void sendTo(String topic, TaskDto taskDto) {
        try {
            kafkaTaskTemplate.send(topic, taskDto);
            kafkaTaskTemplate.flush();
        } catch (Exception e) {
            log.error("Kafka producer error sending message: {} {}", taskDto, e.getMessage());
        }
    }
}