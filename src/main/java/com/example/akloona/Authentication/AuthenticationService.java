package com.example.akloona.Authentication;

import com.example.akloona.AuthenticationController.AuthenticationRequest;
import com.example.akloona.AuthenticationController.AuthenticationResponse;
import com.example.akloona.AuthenticationController.RegisterRequest;
import com.example.akloona.Database.UserRepo;
import com.example.akloona.Database.User_;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final UserRepo userRepo;
    private final TokenRepo tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest registerRequest) {
        try {
            // Log the registration attempt
            log.info("Registering user: {}", registerRequest.getUsername());

            // Switch statement to determine the role of the user based on the account type
            Role role = switch (registerRequest.getAccountType()) {
                case "Customer" -> Role.CUSTOMER;
                case "Staff" -> Role.STAFF;
                case "Manager" -> Role.MANAGER;
                default -> Role.CUSTOMER; // default case, in case none of the cases match
            };

            var user = User_.builder()
                    .username(registerRequest.getUsername())
                    .password(passwordEncoder.encode(registerRequest.getPassword()))
                    .accountType(registerRequest.getAccountType())
                    .role(role)
                    .email(registerRequest.getEmail())
                    .phoneNumber(registerRequest.getPhoneNumber())
                    .address(registerRequest.getAddress())
                    .dob(registerRequest.getDob())
                    .build();

            userRepo.save(user);
            var jwtToken = jwtService.generateToken(user);
            tokenRepository.save(Token.builder().token(jwtToken).user(user).build());

            return AuthenticationResponse.builder().token(jwtToken).build();
        } catch (DataIntegrityViolationException e) {
            log.error("Error registering user: {}", e.getMessage());
            throw new RuntimeException("Email already exists");
        }
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        var user = userRepo.findByUsername(request.getUsername());
        var jwtToken = jwtService.generateToken(user);
        var token = tokenRepository.findByUser_id(user.getId());
        if (token.isPresent()) {
            token.get().setToken(jwtToken);
            token.get().setExpired(false);
            token.get().setRevoked(false);
            tokenRepository.save(token.get());
        } else {
            throw new RuntimeException("User not found please register");
        }

        return AuthenticationResponse.builder().token(jwtToken).build();
    }


    public void saveUserToken(User_ user, String token) {
        var token2 = Token.builder()
                .token(token)
                .user(user)
                .build();
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
            var user = this.userRepo.findByUsername(username);
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
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
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

}