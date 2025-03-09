package com.example.demo.Models;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table
@Data
public class HostDetails {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Integer HostDetailsId;
    @OneToOne
    @JoinColumn(name="user_id")
    private UserEntity userEntity;
    private String firstName;
    private String lastName;
    private String email;
}
