package com.Macharelli.demo.services;

import com.Macharelli.demo.models.Task;
import com.Macharelli.demo.models.User;
import com.Macharelli.demo.repositories.TaskRepository;
import com.Macharelli.demo.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;


    public Task findById(Long id){
        Optional<Task> task = this.taskRepository.findById(id);
        return task.orElseThrow(()->new RuntimeException(
              "Tarefas não encontrada! Id:"+ id + ", Tipo: " + Task.class.getName()
        ));
    }

    public List<Task> findAllByUserId(Long userId){
        List<Task>tasks = this.taskRepository.findByUser_Id(userId);
        return tasks;
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
            throw new RuntimeException("Não é possivel excluir pois há entidades relacionadas!");
        }
    }


}
