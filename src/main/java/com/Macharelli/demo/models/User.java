package com.Macharelli.demo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    public interface CreateUser{}
    public interface UpdateUser{}
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",unique = true)
    private Long id;

    @Column(name = "username",unique = true, length = 100, nullable = false)
    @NotBlank(groups = {CreateUser.class})
    @Size(groups = {CreateUser.class}, min=4, max = 100)
    private String username;
    @Column(name = "password", length = 30, nullable = false)
    @NotBlank(groups = {CreateUser.class,UpdateUser.class})
    @Size(groups = {CreateUser.class,UpdateUser.class},min=6, max = 30)
    private String password;

    @OneToMany(mappedBy = "users")
    private List<Task> tasks = new ArrayList<Task>();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
