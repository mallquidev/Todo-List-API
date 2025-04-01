package com.mallqui.todo_list_api.repositories;

import com.mallqui.todo_list_api.entities.Task;
import com.mallqui.todo_list_api.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    //Encontrar toda las tareas de un usuario especifico
    List<Task> findByUser(User user);

    //Encontrar una tarea por id y usuario (para verificar)
    Optional<Task> findByTaskIdAndUser(Integer taskId, User user);

    //JPQL contar las tareas de un usuario
    @Query("SELECT COUNT(t) FROM Task t WHERE t.user = :user")
    Integer countByUser(@Param("user") User user);

    //Buscar tareas por titulo
    List<Task> findByUserAndTitleContainingIgnoreCase(User user, String title);
}
