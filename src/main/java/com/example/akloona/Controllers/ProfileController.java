package com.example.akloona.Controllers;

import com.example.akloona.Profile.*;
import com.example.akloona.Service.LogoutService;
import com.example.akloona.Service.ProfileUpdatesService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor // lombok annotation to create a constructor with all the required fields
@Getter
@Slf4j // lombok annotation to create a logger instance



public class ProfileController {
    private final ProfileUpdatesService profileUpdatesService;

/**
    //possible PreAuthorize annotations to add to the following endpoints:
//    @GetMapping("/hello")
//    @PreAuthorize("hasRole('CUSTOMER')")
//    @PreAuthorize("hasRole('STAFF')")
//    @PreAuthorize("hasRole('MANAGER')")
//    public String hello() {
//
//        return "Get:: adminConrtolelr";
**/




    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest request, HttpServletRequest httpServletRequest) {
        log.info("Change password request: {}", request);
        return ResponseEntity.ok(profileUpdatesService.changePassword(request));
    }

    @PutMapping("/change-email")
    public ResponseEntity<String> changeEmail(ChangeEmailRequest request, HttpServletRequest httpServletRequest) {

        return ResponseEntity.ok(profileUpdatesService.changeEmail(request, httpServletRequest));
    }

    @PutMapping("/change-address")
    public String changeAddress(@RequestBody ChangeAddressRequest request, HttpServletRequest httpServletRequest) {

        return profileUpdatesService.changeAddress(request, httpServletRequest);
    }

    @PutMapping("/change-phoneNumber")
    public ResponseEntity<String> changePhoneNumber(@RequestBody ChangePhoneNumberRequest request, HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(profileUpdatesService.changePhoneNumber(request, httpServletRequest));
    }
    @DeleteMapping("/delete-account")
    public ResponseEntity<String> deleteAccount(HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(profileUpdatesService.deleteAccount(httpServletRequest));
    }

    @DeleteMapping("/block-user")
    @PreAuthorize("hasAnyRole('MANAGER','STAFF')")
    public ResponseEntity<?> blockUser(@RequestBody BlockUserRequest request) {
        return ResponseEntity.ok(profileUpdatesService.blockUser(request));
    }

    @PutMapping("/unblock-user")
    @PreAuthorize("hasAnyRole('MANAGER','STAFF')")
    public ResponseEntity<?> unblockUser(@RequestBody UnblockUserRequest request) {
        return ResponseEntity.ok(profileUpdatesService.unblockUser(request));
    }



}