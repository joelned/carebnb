package com.example.demo.Repositories;

import com.example.demo.DTOs.DetailsDTO;
import com.example.demo.Models.RefugeeDetails;
import com.example.demo.Models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RefugeeRepository extends JpaRepository<RefugeeDetails, Integer> {
    RefugeeDetails findByName(String name);
    @Query("SELECT new com.example.demo.DTOs.DetailsDTO(r.name, r.age, r.email, r.gender, r.dependents, " +
            "r.countryOfOrigin, r.specialNeeds) " +
            "FROM RefugeeDetails r " +
            "JOIN r.userEntity u " +
            "WHERE u = :userEntity")
    List<DetailsDTO> findRefugeeByUserEntity(UserEntity userEntity);

    RefugeeDetails findByUserEntityUsername(String username);

    RefugeeDetails findByUserEntity(UserEntity userEntity);
}
