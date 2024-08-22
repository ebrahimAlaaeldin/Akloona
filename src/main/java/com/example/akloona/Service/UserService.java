package com.example.akloona.Service;

import com.example.akloona.Database.UserRepo;
import com.example.akloona.Database.User_;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.regex.Pattern;

@Component
public class UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public void registerUser(String username, String rawPassword, String accountType, String email, String phoneNumber, String address, int dob) {
        validatePassword(rawPassword);

        User_ user = new User_(username, passwordEncoder.encode(rawPassword), email, phoneNumber, address, accountType, dob);

        validateUser(user);

        userRepo.save(user);
    }

    private void validatePassword(String password) {
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,}$";
        if (!Pattern.matches(passwordPattern, password)) {
            throw new IllegalArgumentException("Password must be at least 8 characters long, contain at least one digit, one lowercase letter, one uppercase letter, and one special character.");
        }
    }

    private void validateUser(User_ user) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<User_>> violations = validator.validate(user);

        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<User_> violation : violations) {
                sb.append(violation.getMessage()).append("\n");
            }
            throw new IllegalArgumentException(sb.toString());
        }
    }
}
