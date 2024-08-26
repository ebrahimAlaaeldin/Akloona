package com.example.akloona.Token;

import com.example.akloona.Database.User_;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "token") // Ensure the table name is specified
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Ensure the ID generation strategy is specified
    private Integer id;

    @Column(unique = true, nullable = false) // Ensure the column is unique and not null
    private String token;

    @Enumerated(EnumType.STRING)
    private TokenType tokenType = TokenType.BEARER;

    private boolean revoked;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // Ensure the join column is not null
    private User_ user;
}