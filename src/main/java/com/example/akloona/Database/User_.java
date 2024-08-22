package com.example.akloona.Database;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
    @NotBlank(message = "Username is mandatory")
    private String username;

    @Column(name = "password", nullable = false)
    @NotBlank(message = "Password is mandatory")
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is mandatory")
    private String email;

    @Column(name = "phoneNumber", nullable = false, unique = true, length = 11)
    @Pattern(regexp = "01[0125]\\d{8}", message = "Invalid phone number format")
    private String phoneNumber;

    @Column(name = "address", nullable = false, columnDefinition = "TEXT")
    @NotBlank(message = "Address is mandatory")
    private String address;

    @Column(name = "accountType", nullable = false)
    @NotBlank(message = "Account type is mandatory")
    private String accountType;

    @Column(name = "Dob", nullable = false)
    private int YearOfBirth;

    @Column(name = "age", nullable = false)
    private int age;

    public User_(String username, String password, String email, String phoneNumber, String address, String accountType, int dob) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.accountType = accountType;
        this.YearOfBirth = dob;
        this.calculateAge();
    }

    public void calculateAge() {
        this.age = LocalDate.now().getYear() - this.YearOfBirth;
    }

    public int getId() {
        return ID;
    }
}
