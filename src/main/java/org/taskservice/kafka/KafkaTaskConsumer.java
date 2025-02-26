package org.taskservice.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.taskservice.dto.TaskDto;
import org.taskservice.service.NotificationService;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaTaskConsumer {
    @Value("#{'${mail.to}'.split(',')}")
    private List<String> to;

    private final NotificationService notificationService;

    @KafkaListener(id = "${kafka.consumer.group-id}",
            topics = "${kafka.topic.task-name}",
            containerFactory = "kafkaTaskListenerContainerFactory")
    public void listener(@Payload List<TaskDto> messageList,
                         Acknowledgment ack,
                         @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                         @Header(KafkaHeaders.RECEIVED_KEY) String key) {
        log.info("Task consumer: started receiving messages");
        try {
            notificationService.sendEmail(to, "Task notification", messageList);
        } finally {
            ack.acknowledge();
        }
        log.info("Task consumer: messages processed");
    }
}
