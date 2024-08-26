package com.example.akloona.Authentication_;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthenticationResponse {
    @JsonProperty("token")
    private String token;

    @JsonProperty("accessToken")
    private String refreshToken;





}
