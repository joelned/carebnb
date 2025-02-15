package com.example.demo.Repositories;

import com.example.demo.Models.HostDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HostRepository extends JpaRepository<HostDetails, Integer> {
    HostDetails findByUserEntityUsername(String username);
}
