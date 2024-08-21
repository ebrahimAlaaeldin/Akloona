package com.example.akloona.Service;


import com.example.akloona.Database.UserRepo;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.example.akloona.Database.User_;

import java.security.PrivateKey;
import java.util.PrimitiveIterator;


@Getter
@Component
public class MainPage {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder; // for password hashing
    private final UserService userService;
    private User_ activeUser; // for storing the active user

    // for checking if user is logged in and also check the account type for later use it is encapsulated in the class where it can only be modified here
    private  boolean loggedIn ;
    private String accountType ;

    @Autowired
    public MainPage(UserRepo userRepo, PasswordEncoder passwordEncoder, UserService userService) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.loggedIn = false; // default value
        this.accountType = "";
    }

    public String
    login(String accountType, String username, String password) {
        User_ user = userRepo.findByUsernameAndAccountType(username, accountType);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            // if login is successful set the loggedIn to true and set the account type
            this.loggedIn = true;
            this.accountType = accountType;
            this.activeUser = user; // set the active user to the user that logged in
            return "Login Successful" ;

        } else {
            return "Login Failed please check the username and password";
        }
    }

    public void registerUser(String username, String rawPassword, String accountType, String email, String phoneNumber, String address, int dob) throws IllegalStateException {
        if(isLoggedIn()){
            // if the user is already logged in throw an exception
            throw new IllegalStateException("Already Logged In Please Logout First");
        }

        if (userRepo.findByUsername(username) != null) {
            // if the username is already taken throw an exception
            throw new IllegalStateException("Username already taken");
        } else {
            // if the username is not taken register the user
            userService.registerUser(username, rawPassword, accountType, email, phoneNumber, address, dob);

        }
    }


    public void logout() {
        // set the loggedIn to false and set the account type to empty string
        this.loggedIn = false;
        this.accountType = "";
        this.activeUser = null; // set the active user to null

    }

    public void deleteAccount() {
        // delete the account of the active user
        userRepo.delete(activeUser);
        this.activeUser = null; // set the active user to null
        this.loggedIn = false; // set the loggedIn to false
        this.accountType = ""; // set the account type to empty string
    }

    public void updatePassword(String oldPassword, String newPassword) {
        if(passwordEncoder.matches(oldPassword, activeUser.getPassword())){
            // if the old password is correct update the password
            activeUser.setPassword(passwordEncoder.encode(newPassword));
            userRepo.save(activeUser);
        }
        else{
            // if the old password is incorrect throw an exception
            throw new IllegalStateException("Incorrect Password");
        }
    }

    public void updateEmail(String newEmail) {
        // update the email of the active user
        try {
            activeUser.setEmail(newEmail);
            userRepo.save(activeUser);
        } catch (Exception e) {
            throw new IllegalStateException("Invalid Email");
        }

    }

    public void updatePhoneNumber(String newPhoneNumber) {
        try {
            // update the phone number of the active user
            activeUser.setPhoneNumber(newPhoneNumber);
            userRepo.save(activeUser);
        } catch (Exception e) {
            throw new IllegalStateException("Invalid Phone Number");
        }
    }


    public void updateAddress(String newAddress) {
        // update the address of the active user
        activeUser.setAddress(newAddress);
        userRepo.save(activeUser);
    }
}