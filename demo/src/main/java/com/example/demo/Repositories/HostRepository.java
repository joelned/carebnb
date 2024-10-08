package com.example.demo.Repositories;

import com.example.demo.Models.Host;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HostRepository extends JpaRepository<Host, Integer> {
    Host findByEmail(String email);
}
