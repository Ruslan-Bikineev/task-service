package org.taskservice.mapper;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;
import org.taskservice.dto.TaskDto;
import org.taskservice.entity.Task;

@Component
@RequiredArgsConstructor
public class TaskMapper {
    private final ModelMapper modelMapper;

    @PostConstruct
    public void setupMapper() {
        TypeMap<TaskDto, Task> propertyMapper = modelMapper.createTypeMap(TaskDto.class, Task.class);
        propertyMapper.addMappings(mapper -> mapper.skip(Task::setId));
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