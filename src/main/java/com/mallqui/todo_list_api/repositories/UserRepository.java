package com.mallqui.todo_list_api.repositories;

import com.mallqui.todo_list_api.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository //Marca esta interfaz como un componente de acceso a datos(DAO) para que spring lo detecte y registre
public interface UserRepository extends JpaRepository<User, Integer> {
    //1
    Optional<User> findByName(String name); //Busca un usuario por su nombre, puede devolver vacio si no lo encuentra
    boolean existsByName(String name);//Varifica si ya existe un usuario con ese nombre
}
