package com.Macharelli.demo.controllers;

import com.Macharelli.demo.models.User;
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

    public ResponseEntity<Void> createUser(@Valid @RequestBody User user){
        this.userService.create(user);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }
    @PutMapping("/{id}")
    @Validated(User.UpdateUser.class)
    public ResponseEntity<Void> update(@Valid @RequestBody User user,@PathVariable Long id){
        user.setId(id);
        this.userService.update(user);
        return ResponseEntity.noContent().build();

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        this.userService.delete(id);
        return ResponseEntity.noContent().build();
    }




}
