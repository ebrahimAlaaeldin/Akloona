package com.example.akloona.Database;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity(name = "User_")
@NoArgsConstructor
@Table(name = "User_", uniqueConstraints = {
        @UniqueConstraint(name = "email_unique", columnNames = "email"),
        @UniqueConstraint(name = "phoneNumber_unique", columnNames = "phoneNumber"),
        @UniqueConstraint(name = "username_unique", columnNames = "username")
})
public class User_ {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int ID;
    @Column(name = "username", nullable = false, unique = true)
    private String username;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "phoneNumber", nullable = false, unique = true, length = 11)
    private String phoneNumber;
    @Column(name = "address", nullable = false, columnDefinition = "TEXT")
    private String address;
    @Column(name = "accountType", nullable = false)
    private String accountType;

    @Column(name = "Dob", nullable = false)
    private  int YearOfBirth;
    @Column(name = "age", nullable = false)
    private int age;


    public User_(String username, String password, String email, String phoneNumber, String address, String acountType, int dob) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.accountType = acountType;
        this.YearOfBirth = dob;

    }

    public void calculateAge(){
        this.age = LocalDate.now().getYear() - this.YearOfBirth;
    }


    public int getId() {
        return ID;
    }
}
