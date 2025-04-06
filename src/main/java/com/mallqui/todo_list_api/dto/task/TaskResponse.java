package com.mallqui.todo_list_api.dto.task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder    //para crear objetos complejos de manera mas clara y segura
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {

    private Integer taskId;
    private String title;
    private String description;
    private TaskUserResponse user;
}
