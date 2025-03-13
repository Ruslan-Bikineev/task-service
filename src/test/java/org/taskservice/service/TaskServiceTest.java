package org.taskservice.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.taskservice.entity.Task;
import org.taskservice.kafka.KafkaTaskProducer;
import org.taskservice.mapper.TaskMapper;
import org.taskservice.repository.TaskRepository;
import org.taskservice.utils.RandomModels;

import java.util.List;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {
    private Task expectedTask;
    private TaskMapper taskMapper;
    private TaskService taskService;
    private RandomModels randomModels;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private KafkaTaskProducer kafkaTaskProducer;

    public TaskServiceTest() {
        this.randomModels = new RandomModels();
        this.taskMapper = new TaskMapper(new ModelMapper());
    }

    @BeforeEach
    public void setUpBeforeEach() {
        expectedTask = randomModels.getRandomTask();
        taskService = new TaskService(taskMapper, taskRepository, kafkaTaskProducer);
    }

    @Test
    @DisplayName("Получение задачи по ID (задача существует)")
    void testGetByIdExistTask() {
        when(taskRepository.findById(expectedTask.getId())).thenReturn(java.util.Optional.of(expectedTask));
        Task actualTask = taskService.getById(expectedTask.getId());
        Assertions.assertEquals(expectedTask, actualTask);
        verify(taskRepository, times(1)).findById(expectedTask.getId());
    }

    @Test
    @DisplayName("Получение задачи по ID (задача не существует)")
    void testGetByIdNonExistTask() {
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> taskService.getById(expectedTask.getId()));
        verify(taskRepository, times(1)).findById(expectedTask.getId());
    }

    @Test
    @DisplayName("Получение всех задач")
    void testGetAll() {
        List<Task> expectedTasksList = IntStream.range(0, 10)
                .mapToObj(i -> randomModels.getRandomTask())
                .toList();
        when(taskRepository.findAll()).thenReturn(expectedTasksList);
        List<Task> actualTaskList = taskService.getAll();
        Assertions.assertEquals(expectedTasksList, actualTaskList);
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Сохранение задачи")
    void testSave() {
        when(taskRepository.save(any())).thenReturn(expectedTask);
        Task actualTask = taskService.save(expectedTask);
        Assertions.assertEquals(expectedTask, actualTask);
        verify(taskRepository, times(1)).save(expectedTask);
    }

    @Test
    @DisplayName("Обновление задачи (задача существует)")
    void testPutExistTask() {
        when(taskRepository.existsById(any())).thenReturn(true);
        when(taskRepository.save(any())).thenReturn(expectedTask);
        Task actualTask = taskService.put(expectedTask);
        Assertions.assertEquals(expectedTask, actualTask);
        verify(kafkaTaskProducer, times(1)).send(taskMapper.toDto(expectedTask));
        verify(taskRepository, times(1)).save(expectedTask);
        verify(taskRepository, times(1)).existsById(expectedTask.getId());
    }

    @Test
    @DisplayName("Обновление задачи (задача не существует)")
    void testPutNonExistTask() {
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> taskService.put(expectedTask));
        verify(kafkaTaskProducer, times(0)).send(taskMapper.toDto(expectedTask));
        verify(taskRepository, times(1)).existsById(expectedTask.getId());
    }

    @Test
    @DisplayName("Удаление задачи")
    void testDelete() {
        taskService.delete(expectedTask.getId());
        verify(taskRepository, times(1)).deleteById(expectedTask.getId());
    }
}