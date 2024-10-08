package com.example.demo.Repositories;

import com.example.demo.Models.Refugee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefugeeRepository extends JpaRepository<Refugee, Integer> {
   Refugee findByEmail(String email);
}

