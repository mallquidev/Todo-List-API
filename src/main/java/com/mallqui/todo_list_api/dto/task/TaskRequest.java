package com.mallqui.todo_list_api.dto.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TaskRequest {
    @NotBlank(message = "Titulo no puede exceder los 100 caracteres")
    @Size(max = 100)
    private String title;

    @NotBlank(message = "Descripcion no puede estar en blanco")
    @Size(max = 255, message = "Descripcion no debe exceder los 255 caracteres")
    private String description;
}
