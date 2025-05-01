package com.mallqui.todo_list_api.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class JwtEntryPoint implements AuthenticationEntryPoint {
    //7 Punto de entrada jwt
    @Override//sobreescribir metodo
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException{
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No Autorizado");
    }

}
