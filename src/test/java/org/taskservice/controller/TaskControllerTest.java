package org.taskservice.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.taskservice.TaskServiceApplicationTests;
import org.taskservice.dto.TaskDto;
import org.taskservice.entity.Task;
import org.taskservice.mapper.TaskMapper;
import org.taskservice.repository.TaskRepository;
import org.taskservice.utils.RandomModels;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TaskControllerTest extends TaskServiceApplicationTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private RandomModels randomModels;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TaskRepository taskRepository;

    @Test
    @DisplayName("API. GET. /tasks. Get all tasks")
    void testGetAll() throws Exception {
        List<TaskDto> expectedTasksDtoList = taskRepository.findAll()
                .stream()
                .map(taskMapper::toDto)
                .toList();
        MvcResult result = mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
        String responseBody = result.getResponse().getContentAsString();
        List<TaskDto> actualTasksDtoList = objectMapper.readValue(responseBody,
                new TypeReference<>() {
                });
        Assertions.assertEquals(expectedTasksDtoList, actualTasksDtoList);
    }

    @Test
    @DisplayName("API. GET. /tasks/{id}. Get exist task by id")
    void testGetByIdExistTask() throws Exception {
        long existTaskId = 1;
        TaskDto expectedTasksDto = taskMapper.toDto(taskRepository.findById(existTaskId).get());
        MvcResult result = mockMvc.perform(get("/tasks/{id}", existTaskId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
        String responseBody = result.getResponse().getContentAsString();
        TaskDto actualTasksDto = objectMapper.readValue(responseBody,
                new TypeReference<>() {
                });
        Assertions.assertEquals(expectedTasksDto, actualTasksDto);
    }

    @Test
    @DisplayName("API. GET. /tasks/{id}. Get non exist task by id")
    void testGetByIdNonExistTask() throws Exception {
        mockMvc.perform(get("/tasks/{id}", 0))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("API. POST. /tasks. Create task")
    void testSaveTask() throws Exception {
        TaskDto randomTaskDto = randomModels.getRandomTaskDto();
        String randomTaskDtoJson = new ObjectMapper().writeValueAsString(randomTaskDto);
        MvcResult result = mockMvc.perform(post("/tasks")
                        .content(randomTaskDtoJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
        String responseBody = result.getResponse().getContentAsString();
        TaskDto createdTaskDto = objectMapper.readValue(responseBody, TaskDto.class);
        Long createdTaskId = createdTaskDto.getId();
        TaskDto expectedTasksDto = taskMapper.toDto(taskRepository.findById(createdTaskId).get());
        Assertions.assertEquals(expectedTasksDto, createdTaskDto);
        taskRepository.deleteById(createdTaskId);
        Assertions.assertFalse(taskRepository.existsById(createdTaskId));
    }

    @Test
    @DisplayName("API. PUT. /tasks/{id}. Update exist task by id")
    void testPutExistTask() throws Exception {
        Task randomTask = randomModels.getRandomTask();
        randomTask.setId(null);
        randomTask = taskRepository.save(randomTask);
        TaskDto randomTaskDto = randomModels.getRandomTaskDto();
        randomTaskDto.setId(randomTask.getId());
        String randomTaskDtoJson = new ObjectMapper().writeValueAsString(randomTaskDto);
        MvcResult result = mockMvc.perform(put("/tasks/{id}", randomTaskDto.getId())
                        .content(randomTaskDtoJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
        String responseBody = result.getResponse().getContentAsString();
        TaskDto updatedTaskDto = objectMapper.readValue(responseBody, TaskDto.class);
        Assertions.assertEquals(randomTaskDto, updatedTaskDto);
        taskRepository.deleteById(randomTask.getId());
        Assertions.assertFalse(taskRepository.existsById(randomTask.getId()));
    }

    @Test
    @DisplayName("API. PUT. /tasks/{id}. Update non exist task by id")
    void testPutNonExistTask() throws Exception {
        TaskDto randomTaskDto = randomModels.getRandomTaskDto();
        String randomTaskDtoJson = new ObjectMapper().writeValueAsString(randomTaskDto);
        mockMvc.perform(put("/tasks/{id}", randomTaskDto.getId())
                        .content(randomTaskDtoJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("API. Delete. /tasks/{id}. Delete exist task by id")
    void testDeleteExistTask() throws Exception {
        Task randomTask = randomModels.getRandomTask();
        randomTask.setId(null);
        randomTask = taskRepository.save(randomTask);
        mockMvc.perform(delete("/tasks/{id}", randomTask.getId()))
                .andExpect(status().isNoContent());
        Assertions.assertFalse(taskRepository.existsById(randomTask.getId()));
    }
}