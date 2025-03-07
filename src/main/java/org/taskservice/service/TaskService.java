package org.taskservice.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.taskservice.entity.Task;
import org.taskservice.kafka.KafkaTaskProducer;
import org.taskservice.mapper.TaskMapper;
import org.taskservice.repository.TaskRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskMapper taskMapper;
    private final TaskRepository taskRepository;
    private final KafkaTaskProducer kafkaTaskProducer;

    public Task getById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Task with id " + id + " not found"));
    }

    public List<Task> getAll() {
        return taskRepository.findAll();
    }

    public Task save(Task task) {
        return taskRepository.save(task);
    }

    public Task put(Task task) {
        if (taskRepository.existsById(task.getId())) {
            kafkaTaskProducer.send(taskMapper.toDto(task));
            return taskRepository.save(task);
        } else {
            throw new EntityNotFoundException(
                    "Task with id " + task.getId() + " not found");
        }
    }

    public void delete(Long id) {
        taskRepository.deleteById(id);
    }
}