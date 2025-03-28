package com.mallqui.todo_list_api.service;

import com.mallqui.todo_list_api.dto.AuthResponse;
import com.mallqui.todo_list_api.dto.LoginRequest;
import com.mallqui.todo_list_api.dto.RegisterRequest;
import com.mallqui.todo_list_api.entities.Role;
import com.mallqui.todo_list_api.entities.User;
import com.mallqui.todo_list_api.jwt.JwtUtil;
import com.mallqui.todo_list_api.repositories.RoleRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserService userService;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    private final AuthenticationManager authenticationManager;

    public AuthService(UserService userService, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse authenticate(LoginRequest request) {
        //autenitca el usuario
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword())
        );

        //guarda la autenticacion en el contexto de seguridad
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //obtenemos usuarios desde la bd
        User user = userService.findByName(request.getUserName());

        //generamos el token
        String token = jwtUtil.generateToken(authentication);

        return new AuthResponse(token, user.getName(), user.getRole().getName());
    }

    public AuthResponse registerUser(RegisterRequest request) {
        //verificamos si el usuario ya existe
        if (userService.existsByUsername(request.getUserName())) {
            throw new RuntimeException("El usuario ya existe");
        }

        //buscar el rol en la base de datos
        Role roleUser = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Rol USER no encontrado"));

        //crear usuario con contraseña encriptada
        User newUser = new User(
                request.getUserName(),
                passwordEncoder.encode(request.getPassword()),
                roleUser
        );
        //Registramos en la bd y con esto nada mas ya estaria solo si quieres autenticas o solo creas el token y retornamos datos
        userService.save(newUser);

        //Generar token JWT para el nuevo usuario
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //Generar el token
        String token = jwtUtil.generateToken(authentication);

        // 6 retornar respuesta con token y datos básicos
        return new AuthResponse(token, newUser.getName(), newUser.getRole().getName());
    }

}
