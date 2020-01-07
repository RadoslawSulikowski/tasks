package com.crud.tasks.controller;

import com.crud.tasks.domain.TaskDto;
import com.crud.tasks.facade.TaskFacade;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskFacade taskFacade;

    @Test
    public void shouldFetchTasks() throws Exception {
        //Given
        TaskDto taskDto = new TaskDto(1L, "Test title", "Test content");
        List<TaskDto> tasks = new ArrayList<>();
        tasks.add(taskDto);
        when(taskFacade.getTasks()).thenReturn(tasks);

        //When & Then
        mockMvc.perform(get("/v1/tasks").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("Test title")))
                .andExpect(jsonPath("$[0].content", is("Test content")));
    }

    @Test
    public void shouldFetchTask() throws Exception {
        //Given
        TaskDto taskDto = new TaskDto(1L, "Task title", "Task content");
        when(taskFacade.getTask(1L)).thenReturn(taskDto);

        //When & Then
        mockMvc.perform(get("/v1/tasks/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldHandleTaskNotFoundExceptionGetTask() throws Exception {
        //Given
        when(taskFacade.getTask(anyLong())).thenThrow(new TaskNotFoundException());

        //When & Then
        mockMvc.perform(get("/v1/tasks/1"))
                .andExpect(status().is(404))
                .andExpect(status().reason("Task with given id doesn't exist!"));
    }

    @Test
    public void shouldDeleteTask() throws Exception {
        //Given
        doNothing().when(taskFacade).deleteTask(anyLong());
        //When & Then
        mockMvc.perform(delete("/v1/tasks/1"))
                .andExpect(status().is(204))
                .andExpect(status().reason("Task successful deleted"));
    }

    @Test
    public void shouldHandleTaskNotFoundExceptionDeleteTask() throws Exception {
        //Given
        doThrow(new TaskNotFoundException()).when(taskFacade).deleteTask(anyLong());

        //When & Then
        mockMvc.perform(delete("/v1/tasks/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404))
                .andExpect(status().reason("Task with given id doesn't exist!"));
    }

    @Test
    public void shouldReturnUpdatedTask() throws Exception {
        //Given
        TaskDto updatedTask = new TaskDto(1L, "Updated title", "Updated description");
        when(taskFacade.updateTask(any(TaskDto.class))).thenReturn(updatedTask);
        Gson gson = new Gson();
        String jsonContent = gson.toJson(updatedTask);

        //When & Then
        mockMvc.perform(put("/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Updated title")))
                .andExpect(jsonPath("$.content", is("Updated description")));
    }

    @Test
    public void shouldCreateTask() throws Exception {
        //Given
        TaskDto taskDto = new TaskDto(1L, "task Title", "Task content");
        Gson gson = new Gson();
        String jsonContent = gson.toJson(taskDto);
        when(taskFacade.createTask(taskDto)).thenReturn(1L);

        //When & Then
        mockMvc.perform(post("/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonContent))
                .andExpect(status().is(201))
                .andExpect(status().reason("Task successful created"));
    }
}