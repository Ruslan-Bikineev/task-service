package org.taskservice.controller;

import org.springframework.web.bind.annotation.RestController;
import org.taskservice.service.TaskService;

@RestController
public class TaskController {
    private TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }
}