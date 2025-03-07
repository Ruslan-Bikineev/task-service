package org.taskservice.configuration;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.taskservice.dto.TaskDto;
import org.taskservice.kafka.KafkaTaskProducer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfiguration {
    @Value("${kafka.task.bootstrap-server}")
    private String servers;

    @Value("${kafka.task.topic.name}")
    private String topic;

    @Bean
    public ProducerFactory<String, TaskDto> producerTaskFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, false);
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, TaskDto> kafkaTaskTemplate(
            ProducerFactory<String, TaskDto> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    @ConditionalOnProperty(value = "kafka.task.producer.enable",
            havingValue = "true",
            matchIfMissing = true)
    public KafkaTaskProducer producerTask(KafkaTemplate kafkaTaskTemplate) {
        kafkaTaskTemplate.setDefaultTopic(topic);
        return new KafkaTaskProducer(kafkaTaskTemplate);
    }
}