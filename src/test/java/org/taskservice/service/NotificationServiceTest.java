package org.taskservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.taskservice.dto.TaskDto;
import org.taskservice.utils.RandomModels;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {
    private RandomModels randomModels;
    private List<TaskDto> taskDtoList;
    private NotificationService notificationService;

    @Mock
    private JavaMailSender javaMailSender;

    @Captor
    private ArgumentCaptor<SimpleMailMessage> mailCaptor;

    public NotificationServiceTest() {
        this.randomModels = new RandomModels();
    }

    @BeforeEach
    public void setUpBeforeEach() {
        notificationService = new NotificationService(javaMailSender);
        taskDtoList = IntStream.range(0, 5)
                .mapToObj(i -> randomModels.getRandomTaskDto())
                .toList();
    }

    @Test
    @DisplayName("Тестирование отправки email с обновлением задач")
    void testSendEmail() {
        String subject = randomModels.getFaker().lorem().sentence();
        String[] recipients = {randomModels.getFaker().internet().emailAddress(),
                randomModels.getFaker().internet().emailAddress()};
        notificationService.sendEmail(subject, taskDtoList, recipients);
        verify(javaMailSender, times(1)).send(mailCaptor.capture());
        SimpleMailMessage sentMessage = mailCaptor.getValue();
        assertAll(
                () -> assertEquals("taskservice@example.com", sentMessage.getFrom()),
                () -> assertArrayEquals(recipients, sentMessage.getTo()),
                () -> assertEquals(subject, sentMessage.getSubject()),
                () -> IntStream.range(0, taskDtoList.size()).forEach(i ->
                        assertTrue(sentMessage.getText()
                                .contains(taskDtoList.get(i).getDefaultStatusUpdateMessage())))
        );
    }
}