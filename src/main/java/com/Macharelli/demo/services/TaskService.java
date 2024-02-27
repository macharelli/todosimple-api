package com.Macharelli.demo.services;

import com.Macharelli.demo.models.Task;
import com.Macharelli.demo.models.User;
import com.Macharelli.demo.repositories.TaskRepository;
import com.Macharelli.demo.repositories.UserRepository;
import com.Macharelli.demo.services.exceptions.DataBidingViolationException;
import com.Macharelli.demo.services.exceptions.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;


    public Task findById(Long id){
        Optional<Task> task = this.taskRepository.findById(id);
        return task.orElseThrow(()->new ObjectNotFoundException(
              "Tarefas não encontrada! Id:"+ id + ", Tipo: " + Task.class.getName()
        ));
    }
    @Transactional
    public Task create(Task obj){
        User user = this.userService.findById(obj.getUser().getId());
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


}
