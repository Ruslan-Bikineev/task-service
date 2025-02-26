package org.taskservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.taskservice.dto.TaskDto;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class NotificationService {
    private JavaMailSender javaMailSender;

    public NotificationService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(List<String> to, String subject, List<TaskDto> taskList) {
        log.info("Sending email to {}", to);
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("taskservice@example.com");
            message.setTo(to.toArray(new String[0]));
            message.setSubject(subject);
            String emailText = taskList.stream()
                    .map(TaskDto::getDefaultStatusUpdateMessage)
                    .collect(Collectors.joining());
            message.setText(emailText);
            javaMailSender.send(message);
        } catch (Exception e) {
            log.error("Error sending email to: {} {}", to, e.getMessage());
        }
    }
}
