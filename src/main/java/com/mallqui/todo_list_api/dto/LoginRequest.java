package com.mallqui.todo_list_api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    //3
    private String userName;    //campos que el usuario debe enviar al iniciar session(Login)
    private String password;
}
