package org.taskservice.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.taskservice.dto.TaskDto;
import org.taskservice.entity.Task;

@Component
public class TaskMapper {
    private ModelMapper modelMapper;

    public TaskMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Task toEntity(TaskDto taskDto) {
        Task task = modelMapper.map(taskDto, Task.class);
        return task;
    }

    public TaskDto toDto(Task task) {
        TaskDto taskDto = modelMapper.map(task, TaskDto.class);
        return taskDto;
    }
}