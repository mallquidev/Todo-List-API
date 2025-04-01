package com.mallqui.todo_list_api.exceptions.task;

public class TaskNotOwnedException extends RuntimeException {
    public TaskNotOwnedException(Integer taskId) {
        super("Tarea con id " + taskId + " no pertenece al usuario actual");
    }
}
