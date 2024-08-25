package com.example.akloona.Database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.*;
import java.util.Optional;


@Repository
public interface UserRepo extends JpaRepository<User_,Integer> {

    User_ findByUsernameAndAccountType(String username, String accountType);

    User_ findByUsernameAndAccountTypeAndPassword(String username, String accountType, String password);

    Optional<User_> findByUsername(String username);
    void deleteByUsername(String username);

}
