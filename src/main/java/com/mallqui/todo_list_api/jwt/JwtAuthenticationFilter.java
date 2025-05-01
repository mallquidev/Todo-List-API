package com.mallqui.todo_list_api.jwt;

import com.mallqui.todo_list_api.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.rmi.server.ServerCloneException;

@NoArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {//filtro que se ejecuta por peticion
    //8
    @Autowired
    private JwtUtil jwtUtil;//para manejar y validar el token JWT

    @Autowired
    private UserService userService;//Para cargar los datos del usuario desde la bd

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws  ServletException, IOException{
        final String authorizationHeader = request.getHeader("Authorization");
        String username = null;//almacenara el nombre
        String jwt = null;//almacenara el token jwt
        //validamos que el header no sea nulo y empiece con "Bearer"
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);//extraemos el token (ignoramos "Bearer")
            username = jwtUtil.extractUsername(jwt);//Extraemos el nombre de usuario desde el token
        }
        //si hay un username y todavia no hay autenticacion en el contexto de seguridad
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            //cargamos los datos del usuario desde la bd
            UserDetails userDetails = userService.loadUserByUsername(username);
            //validamos el token con los datos del usuario
            if(jwtUtil.validateToken(jwt, userDetails)) {
                //creamos el objeto de autenticacion con el usuario y sus roles(authorities)
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                //Agregamos mas informacion de la peticion http al token(IP, sesion, etc)
                usernamePasswordAuthenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                //Establecemos al usuario autenticado en el contexto de seguridad
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }

        }
        //continuamos con la cadena de filtros(otros filtros o el controlador)
        filterChain.doFilter(request, response);

    }
}
