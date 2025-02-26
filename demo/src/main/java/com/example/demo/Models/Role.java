package com.example.demo.Models;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Integer roleId;
    private String name;

    public Role(String name){
        this.name = name;
    }
    @JsonValue
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId(){
        return roleId;
    }

    public void setId(Integer roleId){
        this.roleId = roleId;
    }

}

