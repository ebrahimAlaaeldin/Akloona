package com.example.akloona.AuthenticationController;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/demo")
@RequiredArgsConstructor //lombok annotation to create a constructor with all the required fields
@Getter
public class DemoController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello World";
    }

}
