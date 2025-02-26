package org.taskservice.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.taskservice.dto.TaskDto;
import org.taskservice.entity.Task;

@Component
@RequiredArgsConstructor
public class TaskMapper {
    private final ModelMapper modelMapper;

    public Task toEntity(TaskDto taskDto) {
        Task task = modelMapper.map(taskDto, Task.class);
        return task;
    }

    public TaskDto toDto(Task task) {
        TaskDto taskDto = modelMapper.map(task, TaskDto.class);
        return taskDto;
    }
}