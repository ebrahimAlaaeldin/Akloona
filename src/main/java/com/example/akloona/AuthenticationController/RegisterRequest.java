package com.example.akloona.AuthenticationController;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisterRequest {

    private String username;
    private String password;
    private String accountType;
    private String email;
    private String phoneNumber;
    private String address;
    private int dob;

}
