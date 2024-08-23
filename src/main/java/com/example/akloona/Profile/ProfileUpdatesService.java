package com.example.akloona.Profile;

import com.example.akloona.Authentication.JwtService;
import com.example.akloona.Database.UserRepo;
import com.example.akloona.Token.TokenRepo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileUpdatesService {

    private final UserRepo userRepo;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenRepo tokenRepo;

    public String changePassword(ChangePasswordRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getOldPassword()
        ));


        var user = userRepo.findByUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepo.save(user);
        return "Password changed successfully";
    }

    public String changeEmail(String Email, HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization").substring(7);
        String username = jwtService.extractUsername(token);

        var user = userRepo.findByUsername(username);
        user.setEmail(Email);
        userRepo.save(user);
        return "Email changed successfully";
    }

    public String changeAddress(String address, HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization").substring(7);
        String username = jwtService.extractUsername(token);

        var user = userRepo.findByUsername(username);
        user.setAddress(address);
        userRepo.save(user);
        return "Address changed successfully";
    }

    public String deleteAccount(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization").substring(7);

        var token2 = tokenRepo.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token not found"));
        var user = tokenRepo.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token not found"))
                .getUser();
        tokenRepo.delete(token2);
        userRepo.delete(user);
        return "Account deleted successfully";
    }

    public String changePhoneNumber(String phoneNumber, HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization").substring(7);
        String username = jwtService.extractUsername(token);

        var user = userRepo.findByUsername(username);
        user.setPhoneNumber(phoneNumber);
        userRepo.save(user);
        return "Phone number changed successfully";
    }
}