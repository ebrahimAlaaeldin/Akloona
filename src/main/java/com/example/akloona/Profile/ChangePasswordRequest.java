package com.example.akloona.Profile;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChangePasswordRequest {
    private String username;
    private String oldPassword;
    private String newPassword;


}
