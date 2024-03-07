package com.Macharelli.demo.services;

import com.Macharelli.demo.models.User;
import com.Macharelli.demo.models.enums.ProfileEnum;
import com.Macharelli.demo.repositories.TaskRepository;
import com.Macharelli.demo.repositories.UserRepository;
import com.Macharelli.demo.security.UserSpringSecurity;
import com.Macharelli.demo.services.exceptions.AuthorizationException;
import com.Macharelli.demo.services.exceptions.DataBidingViolationException;
import com.Macharelli.demo.services.exceptions.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class UserService {

    //@Autowired
    //private UserRepository userRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public User findById(Long id){
        UserSpringSecurity userSpringSecurity = authenticated();
        if (!Objects.nonNull(userSpringSecurity)
                || !userSpringSecurity.hasRole(ProfileEnum.ADMIN)&&!id.equals(userSpringSecurity.getId()))
            throw new AuthorizationException("Acesso negado!");

        Optional<User>user = this.userRepository.findById(id);
        return user.orElseThrow(()-> new ObjectNotFoundException(
                "Usuário não encontrado ! Id: " + id + ", Tipo:" + User.class.getName()));
    }
    @Transactional
    public User create(User obj){
        obj.setId(null);
        obj.setPassword(this.bCryptPasswordEncoder.encode(obj.getPassword()));
        obj.setProfiles(Stream.of(ProfileEnum.USER.getCode()).collect(Collectors.toSet()));
        obj = this.userRepository.save(obj);
        return obj;
    }
    @Transactional
    public User update(User obj){
        User newObj = findById(obj.getId());
        newObj.setPassword(this.bCryptPasswordEncoder.encode(obj.getPassword()));
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

    public static UserSpringSecurity authenticated() {
        try {
            return (UserSpringSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        } catch (Exception e) {
            return null;
        }
    }

}
