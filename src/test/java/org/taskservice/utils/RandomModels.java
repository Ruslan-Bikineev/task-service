package org.taskservice.utils;

import com.github.javafaker.Faker;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.taskservice.dto.TaskDto;
import org.taskservice.entity.Task;

import java.util.Locale;

@Getter
@Component
public class RandomModels {
    private Faker faker;

    public RandomModels() {
        this.faker = new Faker(new Locale("ru"));
    }

    public Task getRandomTask() {
        return new Task(
                faker.random().nextLong(),
                faker.lorem().sentence(),
                faker.lorem().sentence(),
                faker.lorem().sentence(),
                faker.random().nextLong()
        );
    }

    public TaskDto getRandomTaskDto() {
        return new TaskDto(
                faker.random().nextLong(),
                faker.lorem().sentence(),
                faker.lorem().sentence(),
                faker.lorem().sentence(),
                faker.random().nextLong()
        );
    }
}
