package com.example.akloona.Service;

import com.example.akloona.Authentication.JwtService;
import com.example.akloona.Database.UserRepo;
import com.example.akloona.Database.User_;
import com.example.akloona.Enums.UserStatus;
import com.example.akloona.Profile.*;
import com.example.akloona.Token.Token;
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


        var user = userRepo.findByUsername(request.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepo.save(user);
        return "Password changed successfully";
    }

    public String changeEmail(ChangeEmailRequest request, HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization").substring(7);
        String username = jwtService.extractUsername(token);

        var user = userRepo.findByUsername(username).orElseThrow(
                () -> new RuntimeException("User not found")
        );
        user.setEmail(request.getNewEmail());
        userRepo.save(user);
        return "Email changed successfully";
    }

    public String changeAddress(ChangeAddressRequest request, HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization").substring(7);
        String username = jwtService.extractUsername(token);

        var user = userRepo.findByUsername(username).orElseThrow(
                () -> new RuntimeException("User not found")
        );
        user.setAddress(request.getNewAddress());
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

    public String changePhoneNumber(ChangePhoneNumberRequest request, HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization").substring(7);
        String username = jwtService.extractUsername(token);

        var user = userRepo.findByUsername(username).orElseThrow(
                () -> new RuntimeException("User not found")
        );
        user.setPhoneNumber(request.getNewPhoneNumber());
        userRepo.save(user);
        return "Phone number changed successfully";
    }

    public Object blockUser(BlockUserRequest request) {
        var userToBlock = userRepo.findByUsername(request.getUsername()).orElseThrow(
                () -> new RuntimeException("User not found")
        );

        revokeAllTokens((User_) userToBlock);
        userToBlock.setStatus(UserStatus.BLOCKED);
        userRepo.save(userToBlock);
        return "User blocked successfully";
    }


    public Object unblockUser(UnblockUserRequest request) {

        var userToUnblock = userRepo.findByUsername(request.getUsername()).orElseThrow(
                () -> new RuntimeException("User not found")
        );

        activateAllTokens((User_) userToUnblock);
        userToUnblock.setStatus(UserStatus.ACTIVE);
        userRepo.save(userToUnblock);
        return "User unblocked successfully";
    }

    private void activateAllTokens(User_ userToUnblock) {

        tokenRepo.findAllByUser(userToUnblock)
                .forEach(token -> {
                    if (token instanceof Token) {
                        ((Token) token).setRevoked(false); // Cast to Token type
                        tokenRepo.save((Token) token);
                    }
                });
    }

    public void revokeAllTokens(User_ user) {
        tokenRepo.findAllByUser(user)
                .forEach(token -> {
                    if (token instanceof Token) {
                        ((Token) token).setRevoked(true); // Cast to Token type
                        tokenRepo.save((Token) token);
                    }
                });
    }
}