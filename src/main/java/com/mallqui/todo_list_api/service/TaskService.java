package com.mallqui.todo_list_api.service;

import com.mallqui.todo_list_api.dto.task.TaskRequest;
import com.mallqui.todo_list_api.dto.task.TaskResponse;
import com.mallqui.todo_list_api.entities.Task;
import com.mallqui.todo_list_api.entities.User;
import com.mallqui.todo_list_api.exceptions.task.TaskNotFoundException;
import com.mallqui.todo_list_api.exceptions.task.TaskNotOwnedException;
import com.mallqui.todo_list_api.exceptions.task.TaskValidationException;
import com.mallqui.todo_list_api.repositories.TaskRepository;
import com.mallqui.todo_list_api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    // 1. Métodos auxiliares (se definen primero)

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByName(username)
                .orElseThrow(() -> new RuntimeException("Current user not found"));
    }

    private TaskResponse mapToTaskResponse(Task task) {
        return TaskResponse.builder()
                .taskId(task.getTaskId())
                .title(task.getTitle())
                .description(task.getDescription())
                .build();
    }

    private void validateTaskRequest(TaskRequest taskRequest) {
        if(taskRequest.getTitle()==null || taskRequest.getTitle().trim().isEmpty()) {
            throw new TaskValidationException("Task title cannot be empty");
        }
    }

    private void validateTaskOwnership(Task task) {
        User currentUser = getCurrentUser();
        if(!task.getUser().getUserId().equals(currentUser.getUserId())) {
            throw new TaskNotOwnedException(task.getTaskId());
        }
    }

    // 2. Métodos principales del servicio


    @Transactional(readOnly = true)
    public List<TaskResponse> getAllTasks() {
        User currentUser = getCurrentUser();
        List<Task> tasks = taskRepository.findByUser(currentUser);

        return tasks.stream()
                .map(this::mapToTaskResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TaskResponse getTaskById(Integer taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(()-> new TaskNotFoundException(taskId));

        validateTaskOwnership(task);
        return mapToTaskResponse(task);
    }

    @Transactional
    public TaskResponse createTask(TaskRequest taskRequest) {
        validateTaskRequest(taskRequest);
        User currentUser = getCurrentUser();

        Task task = new Task();
        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setUser(currentUser);

        Task savedTask = taskRepository.save(task);
        return mapToTaskResponse(savedTask);
    }

    @Transactional
    public TaskResponse updateTask(Integer taskId, TaskRequest taskRequest) {
        validateTaskRequest(taskRequest);

        Task task = taskRepository.findById(taskId)
                .orElseThrow(()->new TaskNotFoundException(taskId));

        validateTaskOwnership(task);

        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());

        Task updatedTask = taskRepository.save(task);
        return mapToTaskResponse(updatedTask);
    }

    @Transactional
    public void deleteTask(Integer taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(()->new TaskNotFoundException(taskId));

        validateTaskOwnership(task);
        taskRepository.delete(task);
    }



}
