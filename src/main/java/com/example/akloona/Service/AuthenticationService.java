package com.example.akloona.Service;

import com.example.akloona.Authentication.JwtService;
import com.example.akloona.Enums.Role;
import com.example.akloona.Authentication_.AuthenticationRequest;
import com.example.akloona.Authentication_.AuthenticationResponse;
import com.example.akloona.Authentication_.RegisterRequest;
import com.example.akloona.Database.UserRepo;
import com.example.akloona.Database.User_;
import com.example.akloona.Enums.UserStatus;
import com.example.akloona.Token.Token;
import com.example.akloona.Token.TokenRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final UserRepo userRepo;
    private final TokenRepo tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    private void validatePassword(String password) {
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,}$";
        if (!Pattern.matches(passwordPattern, password)) {
            throw new IllegalArgumentException("Password must be at least 8 characters long, contain at least one digit, one lowercase letter, one uppercase letter, and one special character.");
        }
    }
    public AuthenticationResponse register(RegisterRequest registerRequest) {
        try {
            // Log the registration attempt
            log.info("Registering user: {}", registerRequest.getUsername());

            // Switch statement to determine the role of the user based on the account type
            var roleString =registerRequest.getAccountType().toLowerCase();
            //Cast the roleString to Role enum
            Role role = switch (roleString) {
                case "customer" -> Role.CUSTOMER;
                case "staff" -> Role.STAFF;
                case "manager" -> Role.MANAGER;
                default -> throw new IllegalArgumentException("Invalid account type");
            };

            //Validate Password Condition Password must be at least 8 characters long, contain at least one digit, one lowercase letter, one uppercase letter, and one special character
            validatePassword(registerRequest.getPassword());

            var user = User_.builder()
                    .username(registerRequest.getUsername())
                    .password(passwordEncoder.encode(registerRequest.getPassword()))
                    .accountType(registerRequest.getAccountType())
                    .role(role)
                    .email(registerRequest.getEmail())
                    .phoneNumber(registerRequest.getPhoneNumber())
                    .address(registerRequest.getAddress())
                    .dob(registerRequest.getDob())
                    .status(UserStatus.ACTIVE)
                    .build();

            user.calculateAge();
            userRepo.save(user);
            var jwtToken = jwtService.generateToken(user);
            tokenRepository.save(Token.builder().token(jwtToken).user(user).revoked(false).build());

            return AuthenticationResponse.builder().token(jwtToken).build();
        } catch (DataIntegrityViolationException e) {
            log.error("Error registering user: {}", e.getMessage());
            throw new RuntimeException("Email already exists");
        }
    }

    public Object authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
            ));
        } catch (Exception e) {
            return "Authentication failed: " + e.getMessage();
        }

        var user = userRepo.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(user.getStatus().equals(UserStatus.BLOCKED)){
            throw new RuntimeException("User is blocked");
        }
        var jwtToken = jwtService.generateToken(user);
        var token = Token.builder()
                .token(jwtToken)
                .revoked(false)
                .user(user)
                .build();
        tokenRepository.save(token);
        
        return AuthenticationResponse.builder().token(jwtToken).build();
    }


    public void saveUserToken(User_ user, String token) {
        var token2 = Token.builder()
                .token(token)
                .user(user)
                .build();
        tokenRepository.save(token2);
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String username;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        username = jwtService.extractUsername(refreshToken);
        if (username != null) {
            var user = this.userRepo.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user); // revoke all user tokens to prevent token reuse and misuse
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .token(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    private void revokeAllUserTokens(User_ user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getID());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

}