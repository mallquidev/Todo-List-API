package com.mallqui.todo_list_api.exceptions.global;

import com.mallqui.todo_list_api.exceptions.task.TaskNotFoundException;
import com.mallqui.todo_list_api.exceptions.task.TaskNotOwnedException;
import com.mallqui.todo_list_api.exceptions.task.TaskValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler{

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<String> handleTaskNotFoundException(TaskNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(TaskNotOwnedException.class)
    public ResponseEntity<String> handleTaskNotOwnedException(TaskNotOwnedException ex){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    @ExceptionHandler(TaskValidationException.class)
    public ResponseEntity<String> handleTaskValidationException(TaskValidationException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Ocurrio un error Inesperado: " + ex.getMessage());
    }

}
