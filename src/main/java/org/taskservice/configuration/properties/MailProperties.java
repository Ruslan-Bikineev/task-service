package org.taskservice.configuration.properties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "mail")
public record MailProperties(@NotBlank String host,
                             @Positive int port,
                             @NotBlank String username,
                             @NotBlank String password,
                             @NotBlank String protocol,
                             @NotEmpty @Size(min = 1) List<String> to) {

    public MailProperties(String host, int port, String username,
                          String password, String protocol, String to) {
        this(host, port, username, password, protocol, List.of(to.split(",")));
    }
}