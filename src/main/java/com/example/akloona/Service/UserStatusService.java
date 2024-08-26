package com.example.akloona.Service;

import com.example.akloona.Database.UserRepo;
import com.example.akloona.Enums.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserStatusService {
    private final UserRepo userRepo;

    public boolean hasStatus(String username, UserStatus status) {
        return userRepo.findByUsername(username)
                .map(user -> user.getStatus().equals(status))
                .orElse(false);
    }
}