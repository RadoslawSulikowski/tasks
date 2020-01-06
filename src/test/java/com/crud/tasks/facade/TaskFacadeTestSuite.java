package com.crud.tasks.facade;

import com.crud.tasks.controller.TaskNotFoundException;
import com.crud.tasks.domain.Task;
import com.crud.tasks.domain.TaskDto;
import com.crud.tasks.mapper.TaskMapper;
import com.crud.tasks.service.DbService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TaskFacadeTestSuite {

    private static final Long TEST_TASK_ID = 1L;
    private static final String TEST_TASK_TITLE = "Task title";
    private static final String TEST_TASK_CONTENT = "Task content";

    @InjectMocks
    private TaskFacade taskFacade;

    @Mock
    private DbService service;

    @Mock
    private TaskMapper taskMapper;

    @Test(expected = TaskNotFoundException.class)
    public void shouldTrowTaskNotFoundExceptionGetTask() throws TaskNotFoundException {
        //Given
        when(service.getTask(anyLong())).thenReturn(Optional.empty());

        //When
        taskFacade.getTask(1L);

        //Then
        //throw TaskNotFoundException
    }

    @Test
    public void shouldFetchTask() throws TaskNotFoundException {
        //Given
        Task task = new Task(TEST_TASK_ID, TEST_TASK_TITLE, TEST_TASK_CONTENT);
        TaskDto taskDto = new TaskDto(TEST_TASK_ID, TEST_TASK_TITLE, TEST_TASK_CONTENT);
        when(taskMapper.mapToTaskDto(task)).thenReturn(taskDto);
        when(service.getTask(anyLong())).thenReturn(Optional.of(task));

        //When
        TaskDto resultTask = taskFacade.getTask(1L);

        //Then
        assertEquals(TEST_TASK_ID, resultTask.getId());
        assertEquals(TEST_TASK_TITLE, resultTask.getTitle());
        assertEquals(TEST_TASK_CONTENT, resultTask.getContent());
    }

    @Test(expected = TaskNotFoundException.class)
    public void shouldTrowTaskNotFoundExceptionDeleteTask() throws TaskNotFoundException {
        //Given
        doThrow(NullPointerException.class).when(service).deleteTask(anyLong());
        //When
        taskFacade.deleteTask(1L);

        //Then
        //throw TaskNotFoundException
    }

    @Test
    public void  shouldReturnUpdatedTask() {
        //Given
        TaskDto taskDto = new TaskDto(TEST_TASK_ID, TEST_TASK_TITLE, TEST_TASK_CONTENT);
        Task task = new Task(TEST_TASK_ID, TEST_TASK_TITLE, TEST_TASK_CONTENT);
        when(taskMapper.mapToTask(taskDto)).thenReturn(task);
        when(service.saveTask(task)).thenReturn(task);
        when(taskMapper.mapToTaskDto(task)).thenReturn(taskDto);

        //When
        TaskDto updatedTask = taskFacade.updateTask(taskDto);

        //Then
        assertNotNull(updatedTask);
        assertEquals(TEST_TASK_ID, updatedTask.getId());
        assertEquals(TEST_TASK_TITLE, updatedTask.getTitle());
        assertEquals(TEST_TASK_CONTENT, updatedTask.getContent());
    }
}
