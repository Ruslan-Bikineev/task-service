package org.taskservice.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.taskservice.aspect.annotation.ControllerLogging;
import org.taskservice.dto.TaskDto;
import org.taskservice.entity.Task;
import org.taskservice.mapper.TaskMapper;
import org.taskservice.service.TaskService;

import java.util.List;

@ControllerLogging
@RestController
@RequestMapping("/tasks")
public class TaskController {
    private TaskMapper taskMapper;
    private TaskService taskService;

    public TaskController(TaskMapper taskMapper,
                          TaskService taskService) {
        this.taskMapper = taskMapper;
        this.taskService = taskService;
    }

    @GetMapping
    public List<Task> getAll() {
        return taskService.getAll();
    }

    @GetMapping("/{id}")
    public TaskDto getById(@PathVariable("id") Long id) {
        Task task = taskService.getById(id);
        return taskMapper.toDto(task);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDto save(@Valid
                        @RequestBody TaskDto taskDto) {
        Task task = taskMapper.toEntity(taskDto);
        task = taskService.save(task);
        return taskMapper.toDto(task);
    }

    @PutMapping("/{id}")
    public TaskDto put(@PathVariable("id") Long id,
                       @Valid @RequestBody TaskDto taskDto) {
        taskDto.setId(id);
        Task task = taskMapper.toEntity(taskDto);
        task = taskService.put(task);
        return taskMapper.toDto(task);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        taskService.delete(id);
    }
}