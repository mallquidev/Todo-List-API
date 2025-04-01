package com.mallqui.todo_list_api.exceptions.task;

public class TaskNotFoundException extends RuntimeException{
    public TaskNotFoundException(Integer taskId) {
        super("Tarea con id " + taskId + " no encontrada");
    }
}
