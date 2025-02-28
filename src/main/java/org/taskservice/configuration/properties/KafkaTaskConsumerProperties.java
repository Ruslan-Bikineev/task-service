package org.taskservice.configuration.properties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kafka.task.consumer")
public record KafkaTaskConsumerProperties(@NotBlank String groupId,
                                          @Positive Integer sessionTimeout,
                                          @Positive Integer maxPartitionFetchBytes,
                                          @Positive Integer maxPollRecords,
                                          @Positive Integer maxPollIntervals) {
}
