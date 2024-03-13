package com.Macharelli.demo.services;

import com.Macharelli.demo.models.Task;
import com.Macharelli.demo.models.User;
import com.Macharelli.demo.models.enums.ProfileEnum;
import com.Macharelli.demo.models.projection.TasksProjection;
import com.Macharelli.demo.repositories.TaskRepository;
import com.Macharelli.demo.security.UserSpringSecurity;
import com.Macharelli.demo.services.exceptions.AuthorizationException;
import com.Macharelli.demo.services.exceptions.DataBidingViolationException;
import com.Macharelli.demo.services.exceptions.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;


    public Task findById(Long id){
        Task task = this.taskRepository.findById(id).orElseThrow(()->new ObjectNotFoundException(
              "Tarefa não encontrada! Id:"+ id + ", Tipo: " + Task.class.getName()));

        UserSpringSecurity userSpringSecurity = UserService.authenticated();
        if (Objects.isNull(userSpringSecurity)||!userSpringSecurity.hasRole(ProfileEnum.ADMIN)&&!userHasTask(userSpringSecurity,task)){
            throw new AuthorizationException("Acesso negado!");
        }

        return task;
    }
    @Transactional
    public Task create(Task obj){
        UserSpringSecurity userSpringSecurity = UserService.authenticated();
        if (Objects.isNull(userSpringSecurity)){
            throw new AuthorizationException("Acesso negado!");
        }
        User user = this.userService.findById(userSpringSecurity.getId());
        obj.setId(null);
        obj.setUser(user);
        obj = this.taskRepository.save(obj);
        return obj;
    }
    @Transactional
    public Task update(Task obj){
        Task newObj = findById(obj.getId());
        newObj.setDescription(obj.getDescription());
        return this.taskRepository.save(newObj);
    }

    public void delete(Long id){
        findById(id);
        try{
            this.taskRepository.deleteById(id);
        }catch (RuntimeException e){
            throw new DataBidingViolationException("Não é possivel excluir pois há entidades relacionadas!");
        }
    }
    public List<TasksProjection> findAllByUser(){
        UserSpringSecurity userSpringSecurity = UserService.authenticated();
        if (Objects.isNull(userSpringSecurity)){
            throw new AuthorizationException("Acesso negado!");
        }
        List<TasksProjection>tasks = this.taskRepository.findByUser_Id(userSpringSecurity.getId());
        return tasks;
    }

    private boolean userHasTask(UserSpringSecurity userSpringSecurity, Task task){
        return task.getUser().getId().equals(userSpringSecurity.getId());
    }


}
