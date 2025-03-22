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
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
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

    public boolean existsByUsername(String userName){
        return userRepository.existsByName(userName);
    }

    public void save(User user){
        userRepository.save(user);
    }
}
