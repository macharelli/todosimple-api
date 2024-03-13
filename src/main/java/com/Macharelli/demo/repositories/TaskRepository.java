package com.Macharelli.demo.repositories;

import com.Macharelli.demo.models.Task;
import com.Macharelli.demo.models.projection.TasksProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<TasksProjection> findByUser_Id(Long id);

   // @Query(value = "SELECT t FROM Task t WHERE t.user.id= :id ")
    //List<Task> findByUser_Id(@Param("id") Long id);

   // @Query(value = "SELECT * FROM tasks t WHERE t.user_id = :id",nativeQuery = true)
    //List<Task> findByUser_Id(@Param("id") Long id);
}
