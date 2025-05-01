package com.mallqui.todo_list_api.config;

import com.mallqui.todo_list_api.jwt.JwtAuthenticationFilter;
import com.mallqui.todo_list_api.jwt.JwtEntryPoint;
import com.mallqui.todo_list_api.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


@EnableWebSecurity
@Configuration
public class SecurityConfig {
    //9
    /*@Bean esto es para iniciar antes ya que si no no dejara probar las apis
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults())
                .csrf(csrf->csrf.disable())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/test/hello").permitAll();
                    auth.requestMatchers("/auth/login/", "/auth/register").permitAll();
                    auth.anyRequest().authenticated();
                });
        return http.build();
    }*/

    @Bean//define un bean para el filtro de seguridad (SecurityFilterChain)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //configura CORS con valores pordefecto
        http.cors(Customizer.withDefaults())
                //desactiva CSRF (ya que usamo JWT, no necesitamos proteccion CSRF)
                .csrf(csrf-> csrf.disable())
                .httpBasic(Customizer.withDefaults())//habilita autenticacion básica
                //define que no usara sesion (cada peticion es independiente: Stateless)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //configura las rutas publicas y protegidas
                .authorizeHttpRequests(auth ->{
                    auth.requestMatchers("/auth/login", "/auth/register").permitAll();
                    auth.anyRequest().authenticated();
                })
                //Maneja errores de autenticacion con nuestra clase personalizada JwtEntryPoint
                .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtEntryPoint()));
        return http.build();
    }
    //Define un bean de AuthenticationManager, necesario para la autenticacion con login
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Inyecta el servicio personalizado que implementa UserDetailsService
    private final UserService userService;

    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public JwtEntryPoint jwtEntryPoint() {return new JwtEntryPoint();}

    //Define el filtro JWT que se ejecutara en cada request
    @Bean
    public JwtAuthenticationFilter jwtTokenFilter(){
        return new JwtAuthenticationFilter();
    }

    // Define el codificador de contraseñas (usa BCrypt, que es seguro y estándar)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Indica que UserService es el encargado de cargar los usuarios (implementa UserDetailsService)
    @Bean
    public UserDetailsService userDetailsService() {
        return userService;
    }

    // Define el proveedor de autenticación que usa UserDetailsService y codificador de contraseña
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userService); // Le pasamos nuestro servicio
        authenticationProvider.setPasswordEncoder(passwordEncoder()); // Le pasamos el codificador de contraseñas
        return authenticationProvider;
    }

    // Configura el CORS para permitir peticiones desde el frontend (por ejemplo, Angular en localhost:4200)
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Permitir frontend desde esta URL
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));

        // Métodos HTTP permitidos
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Headers que se permitirán en la solicitud
        configuration.setAllowedHeaders(List.of("Authorization", "Content-type"));

        // Permitir enviar cookies y headers de autenticación
        configuration.setAllowCredentials(true);

        // Aplicar esta configuración a todas las rutas
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}

