package org.taskservice.repository;

import org.springframework.data.repository.ListCrudRepository;
import org.taskservice.entity.Task;

public interface TaskRepository extends ListCrudRepository<Task, Long> {
}