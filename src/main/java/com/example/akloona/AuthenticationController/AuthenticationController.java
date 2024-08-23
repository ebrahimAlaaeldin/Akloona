package com.example.akloona.AuthenticationController;


import com.example.akloona.Authentication.AuthenticationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.akloona.Service.MainPage;

import java.io.IOException;

@RestController
@RequestMapping("/api/authenticate")
@RequiredArgsConstructor //lombok annotation to create a constructor with all the required fields
@Getter
public class AuthenticationController {
    @Autowired
    private final MainPage mainPage;
    private final AuthenticationService authenticationService;




        @PostMapping("/register")
        public ResponseEntity<AuthenticationResponse> registerUser(@RequestBody RegisterRequest request) {
            return ResponseEntity.ok(authenticationService.register(request));

        }

        @PostMapping("/login")
        public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {

            return ResponseEntity.ok(authenticationService.authenticate(request));

        }

    @PostMapping("/refresh-token") //
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        authenticationService.refreshToken(request, response);
    }



//
//
//        @GetMapping("/logout")
//        public String logout() {
//
//            if(mainPage.isLoggedIn()){
//                mainPage.logout();
//                return "Logged Out";
//            }
//            return "Please login first";
//
//        }
//        @GetMapping("/getActiveUser")
//        public User_ getActiveUser() {
//            return mainPage.getActiveUser();
//        }
//
//    @PutMapping("/updatePassword")
//    public String updatePassword(@RequestParam("oldPassword") String oldPassword,
//                                 @RequestParam("newPassword") String newPassword) {
//
//        if(mainPage.isLoggedIn()){
//            try {
//                mainPage.updatePassword(oldPassword, newPassword);
//                return "Password Updated";
//            } catch (Exception e) {
//                return "Password Update Failed " + e.getMessage();
//            }
//        }
//        return "Not Logged In";
//    }
//
//    @PutMapping("/updateEmail")
//    public String updateEmail(@RequestParam("newEmail") String newEmail) {
//        if(mainPage.isLoggedIn()){
//            mainPage.updateEmail(newEmail);
//            return "Email Updated";
//        }
//        return "Not Logged In";
//    }
//
//    @PutMapping("/updatePhoneNumber")
//    public String updatePhoneNumber(@RequestParam("newPhoneNumber") String newPhoneNumber) {
//        if(mainPage.isLoggedIn()){
//            try {
//                mainPage.updatePhoneNumber(newPhoneNumber);
//            }
//            catch (Exception e) {
//                return "Phone Number Update Failed " + e.getMessage();
//            }
//        }
//        return "Not Logged In";
//    }
//
//    @PutMapping("/updateAddress")
//    public String updateAddress(@RequestParam("newAddress") String newAddress) {
//        if(mainPage.isLoggedIn()){
//            mainPage.updateAddress(newAddress);
//            return "Address Updated";
//        }
//        return "Not Logged In";
//    }
//
//    @DeleteMapping("/deleteAccount")
//    public String deleteAccount() {
//        if(mainPage.isLoggedIn()){
//            mainPage.deleteAccount();
//            return "Account Deleted";
//        }
//        return "Not Logged In";
//
//    }
}
