package com.example.spring_boot_security.Repo;

import com.example.spring_boot_security.Model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<Users,Integer> {
    Users findByusername(String username );
}
