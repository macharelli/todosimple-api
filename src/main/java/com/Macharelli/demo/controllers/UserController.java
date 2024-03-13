package com.Macharelli.demo.controllers;

import com.Macharelli.demo.models.User;
import com.Macharelli.demo.models.dto.UserCreateDTO;
import com.Macharelli.demo.models.dto.UserUpdateDTO;
import com.Macharelli.demo.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id){
        User user = this.userService.findById(id);
        return ResponseEntity.ok().body(user);

    }
    @PostMapping
    public ResponseEntity<Void> createUser(@Valid @RequestBody UserCreateDTO userCreateDTO){
        User user = this.userService.fromDTO(userCreateDTO);
        User newUser = this.userService.create(user);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(newUser.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }
    @PutMapping("/{id}")

    public ResponseEntity<Void> update(@Valid @RequestBody UserUpdateDTO userUpdateDTO, @PathVariable Long id){
        userUpdateDTO.setId(id);
        User user = this.userService.fromDTO(userUpdateDTO);
        this.userService.update(user);
        return ResponseEntity.noContent().build();

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        this.userService.delete(id);
        return ResponseEntity.noContent().build();
    }




}
