package com.mallqui.todo_list_api.service;

import com.mallqui.todo_list_api.entities.User;
import com.mallqui.todo_list_api.repositories.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserService implements UserDetailsService {
    //5
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override //Sobreescribir metodo del metodo UserDetailsService
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException{
        User user = userRepository.findByName(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().getName());

        return new org.springframework.security.core.userdetails.User(
                user.getName(),
                user.getPassword(),
                Collections.singleton(authority)
        );
    }
    //Buscar usuario
    public User findByName(String userName) {
        return userRepository.findByName(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
    }
    //existe el usuario?
    public boolean existsByUsername(String userName){
        return userRepository.existsByName(userName);
    }
    //guardar
    public void save(User user){
        userRepository.save(user);
    }
}
