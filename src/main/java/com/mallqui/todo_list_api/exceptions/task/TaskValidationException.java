package com.mallqui.todo_list_api.exceptions.task;

public class TaskValidationException extends RuntimeException{
    public TaskValidationException(String message) {
        super(message);
    }
}
