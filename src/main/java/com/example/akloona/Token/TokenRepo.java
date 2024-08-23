package com.example.akloona.Token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepo extends JpaRepository<Token, Long> {

    @Query("SELECT t FROM Token t WHERE t.user.ID= :userId AND (t.expired = false OR t.revoked = false)")
    List<Token> findAllValidTokenByUser(Integer userId);

    Optional<Token> findByToken(String token);

}
