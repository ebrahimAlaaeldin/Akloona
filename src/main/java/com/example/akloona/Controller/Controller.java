package com.example.akloona.Controller;


import com.example.akloona.Database.User_;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.akloona.Service.MainPage;
@RestController
@RequestMapping("/api")
@Getter
public class Controller {
    @Autowired
    private final MainPage mainPage;
    public Controller(MainPage mainPage) {
        this.mainPage = mainPage;
    }


        @GetMapping("/hello")
        public String hello() {
            return "Hello World";
        }
        @PostMapping("/login")
        public String login(@RequestParam("username") String username,
                            @RequestParam("password") String password,
                            @RequestParam("accountType") String accountType) {

            // check if the user is already logged in
            if(mainPage.isLoggedIn()){
                return "Already Logged In";
            }
            return mainPage.login(accountType, username, password);


        }

        @PostMapping("/register")
        public String registerUser(@RequestParam("username") String username,
                                 @RequestParam("password") String password,
                                 @RequestParam("accountType") String accountType,
                                 @RequestParam("email") String email,
                                 @RequestParam("phoneNumber") String phoneNumber,
                                 @RequestParam("address") String address,
                                 @RequestParam("dob") int dob) {

         try {
             mainPage.registerUser(username, password, accountType, email, phoneNumber, address, dob);
                return "Registration Successful";
         }
            catch (Exception e) {
                return "Registration Failed " + e.getMessage();
            }


        }

        @GetMapping("/logout")
        public String logout() {

            if(mainPage.isLoggedIn()){
                mainPage.logout();
                return "Logged Out";
            }
            return "Please login first";

        }
        @GetMapping("/getActiveUser")
        public User_ getActiveUser() {
            return mainPage.getActiveUser();
        }

    @PutMapping("/updatePassword")
    public String updatePassword(@RequestParam("oldPassword") String oldPassword,
                                 @RequestParam("newPassword") String newPassword) {

        if(mainPage.isLoggedIn()){
            try {
                mainPage.updatePassword(oldPassword, newPassword);
                return "Password Updated";
            } catch (Exception e) {
                return "Password Update Failed " + e.getMessage();
            }
        }
        return "Not Logged In";
    }

    @PutMapping("/updateEmail")
    public String updateEmail(@RequestParam("newEmail") String newEmail) {
        if(mainPage.isLoggedIn()){
            mainPage.updateEmail(newEmail);
            return "Email Updated";
        }
        return "Not Logged In";
    }

    @PutMapping("/updatePhoneNumber")
    public String updatePhoneNumber(@RequestParam("newPhoneNumber") String newPhoneNumber) {
        if(mainPage.isLoggedIn()){
            try {
                mainPage.updatePhoneNumber(newPhoneNumber);
            }
            catch (Exception e) {
                return "Phone Number Update Failed " + e.getMessage();
            }
        }
        return "Not Logged In";
    }

    @PutMapping("/updateAddress")
    public String updateAddress(@RequestParam("newAddress") String newAddress) {
        if(mainPage.isLoggedIn()){
            mainPage.updateAddress(newAddress);
            return "Address Updated";
        }
        return "Not Logged In";
    }

    @DeleteMapping("/deleteAccount")
    public String deleteAccount() {
        if(mainPage.isLoggedIn()){
            mainPage.deleteAccount();
            return "Account Deleted";
        }
        return "Not Logged In";

    }
}
