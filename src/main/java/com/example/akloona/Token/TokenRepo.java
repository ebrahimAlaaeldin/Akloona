package com.example.akloona.Token;


import com.example.akloona.Database.User_;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepo extends JpaRepository<Token, Long> {

    @Query("SELECT t FROM Token t WHERE t.user.ID = :userId AND ( t.revoked = false)")
    List<Token> findAllValidTokenByUser(Integer userId);

    Optional<Token> findByToken(String token);

    Optional<Token> findByUser_ID(Integer userId);

    Optional<Token> findByUser(User_ userToBlock);

    Iterable<Object> findAllByUser(User_ user);
}