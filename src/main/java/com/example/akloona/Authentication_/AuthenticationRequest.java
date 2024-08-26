package com.example.akloona.Authentication_;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthenticationRequest {
    private String username;
    private String password;

}
