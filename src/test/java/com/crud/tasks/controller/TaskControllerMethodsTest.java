package com.crud.tasks.controller;

import com.crud.tasks.domain.Task;
import com.crud.tasks.domain.TaskDto;
import com.crud.tasks.mapper.TaskMapper;
import com.crud.tasks.repository.TaskRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TaskControllerMethodsTest {

    private static final String TASK_TITLE = "Task title";
    private static final String TASK_CONTENT = "Task content";

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskController taskController;

    @Autowired
    private TaskMapper taskMapper;

    @Test
    public void testCreateTaskMethod() {
        //Given
        TaskDto taskDto = new TaskDto(TASK_TITLE, TASK_CONTENT);

        //When
        Long taskId = taskController.createTask(taskDto);

        //Then
        assertTrue(taskRepository.findById(taskId).isPresent());

        //CleanUp
        taskRepository.deleteById(taskId);
    }

    @Test(expected = TaskNotFoundException.class)
    public void testGetTaskMethodThrowsTaskNotFoundException() throws TaskNotFoundException {
        //Given
        //When
        taskController.getTask(-1L);
        //Then
        //throw TaskNotFoundException
        //CleanUp
    }

    @Test()
    public void testGetTaskMethod() throws TaskNotFoundException {
        //Given
        Task task = taskRepository.save(taskMapper.mapToTask(new TaskDto(TASK_TITLE, TASK_CONTENT)));
        Long taskId = task.getId();

        //When
        TaskDto fetchedTask = taskController.getTask(taskId);

        //Then
        assertEquals(taskId, fetchedTask.getId());
        assertEquals(TASK_TITLE, fetchedTask.getTitle());
        assertEquals(TASK_CONTENT, fetchedTask.getContent());

        //CleanUp
        taskRepository.deleteById(taskId);
    }

    @Test
    public void testGetTasksMethod() {
        //Given
        List<Task> tasks = taskRepository.findAll();

        //When
        List<TaskDto> fetchedTasks = taskController.getTasks();

        //Then
        assertEquals(tasks.size(), fetchedTasks.size());

        //CleanUp

    }
}
