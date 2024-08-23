package com.example.akloona.Service;

import com.example.akloona.Database.UserRepo;
import com.example.akloona.Database.User_;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class UserService {

        private final UserRepo userRepo;
        private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        public UserService(UserRepo userRepo) {
            this.userRepo = userRepo;
        }

    public void registerUser(String username, String rawPassword, String accountType, String email, String phoneNumber, String address, int dob) {
        User_ user = new User_();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setAccountType(accountType);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        user.setAddress(address);
        user.setDob(dob);

        user.calculateAge();
        userRepo.save(user);
    }



}


