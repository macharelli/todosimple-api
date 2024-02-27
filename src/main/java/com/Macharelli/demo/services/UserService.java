package com.Macharelli.demo.services;

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
public class UserService {

    //@Autowired
    //private UserRepository userRepository;
    private final UserRepository userRepository;

    public User findById(Long id){
        Optional<User>user = this.userRepository.findById(id);
        return user.orElseThrow(()-> new ObjectNotFoundException(
                "Usuário não encontrado ! Id: " + id + ", Tipo:" + User.class.getName()));
    }
    @Transactional
    public User create(User obj){
        obj.setId(null);
        obj = this.userRepository.save(obj);
        return obj;
    }
    @Transactional
    public User update(User obj){
        User newObj = findById(obj.getId());
        newObj.setPassword(obj.getPassword());
        return this.userRepository.save(newObj);

    }

    public void delete(Long id){
        findById(id);
        try{
            this.userRepository.deleteById(id);
        }catch(RuntimeException e){
            throw new DataBidingViolationException("Não é possivel excluir pois há entidades relacionadas!");
        }
    }



}
