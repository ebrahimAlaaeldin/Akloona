// package com.example.akloona.repository;

// import com.example.akloona.Database.*;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import java.time.LocalDate;

// @SpringBootTest
// public class UserRepositoryTest {
//     @Autowired
//     private UserRepository userRepository;

//     @Test
//     void saveMethod()
//     {
//         User_ newUser = new User_("omaressam","omar_123","omaressam.ee12@gmail.com","01011941710","khalil St, Mohandseen","admin",LocalDate.of(2001, 8, 28));
//         User_ savedUser=userRepository.save(newUser);
//         System.out.println(savedUser.getID());
//         System.out.println(savedUser.getAge());
//         System.out.println(savedUser.getUsername());
//     }
// }
