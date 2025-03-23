package org.taskservice.configuration.properties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "mail")
public record MailProperties(@NotBlank String host,
                             @Positive int port,
                             @NotBlank String username,
                             @NotBlank String password,
                             @NotBlank String protocol,
                             @NotBlank
                             @Pattern(regexp = "^([A-Za-z0-9-_.]+@[a-z0-9-]+\\.[a-z]{2,6})(\\s[A-Za-z0-9-_.]+@[a-z0-9-]+\\.[a-z]{2,6})*$",
                                     message = "Invalid email address or must be a space separated list of email addresses")
                             String to) {
}