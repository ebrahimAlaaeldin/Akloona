package com.example.akloona.Profile;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor // lombok annotation to create a constructor with all the required fields
@Getter
@Slf4j // lombok annotation to create a logger instance

public class ProfileController {
    private final ProfileUpdatesService profileUpdatesService;

    @GetMapping("/hello")
    public String hello() {
        return "Hello World";
    }


    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest request) {
        log.info("Change password request: {}", request);
        return ResponseEntity.ok(profileUpdatesService.changePassword(request));
    }

    @PutMapping("/change-email")
    public ResponseEntity<String> changeEmail(@RequestParam String Email, HttpServletRequest httpServletRequest) {
        log.info("Change email request: {}", Email);
        return ResponseEntity.ok(profileUpdatesService.changeEmail(Email, httpServletRequest));
    }

    @PutMapping("/change-address")
    public String changeAddress(@RequestParam String address, HttpServletRequest httpServletRequest) {
        log.info("Change address request: {}", address);
        return profileUpdatesService.changeAddress(address, httpServletRequest);
    }

    @PutMapping("/change-phoneNumber")
    public ResponseEntity<String> changePhoneNumber(@RequestParam String phoneNumber, HttpServletRequest httpServletRequest) {
        log.info("Change phone number request: {}", phoneNumber);
        return ResponseEntity.ok(profileUpdatesService.changePhoneNumber(phoneNumber, httpServletRequest));
    }
    @DeleteMapping("/delete-account")
    public ResponseEntity<String> deleteAccount(HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(profileUpdatesService.deleteAccount(httpServletRequest));
    }

}