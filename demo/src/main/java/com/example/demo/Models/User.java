package com.example.demo.Models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class User {
    private String username;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name= "user_roles", joinColumns = @JoinColumn(name="user_id", referencedColumnName = "userId")
    ,inverseJoinColumns = @JoinColumn(name="role_id", referencedColumnName = "roleId")
    )
    private List<Role> roles;
}
