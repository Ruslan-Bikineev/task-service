package org.taskservice.service;

import org.springframework.stereotype.Service;
import org.taskservice.repository.TaskRepository;

@Service
public class TaskService {
    private TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
}