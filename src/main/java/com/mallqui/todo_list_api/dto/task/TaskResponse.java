package com.mallqui.todo_list_api.dto.task;

import lombok.Data;

@Data
public class TaskResponse {

    private Integer taskId;
    private String title;
    private String description;
    private TaskUserResponse user;
}
