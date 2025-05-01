package com.mallqui.todo_list_api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    //4
    private String userName;
    private String password; //campos que el usuario debe enviar para registrarse en el sitema
}
