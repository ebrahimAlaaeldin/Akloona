package com.example.akloona.repository;
import com.example.akloona.Database.User_;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository < User_ , Integer> {
    
    
}
