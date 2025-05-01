package com.mallqui.todo_list_api.repositories;

import com.mallqui.todo_list_api.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    //2
    Optional<Role> findByName(String name); //Busca un rol por su nombre(como "ADMIN" o "USER"), devuelve vacio por si no existe
}
