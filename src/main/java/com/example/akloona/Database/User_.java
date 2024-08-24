package com.example.akloona.Database;


import com.example.akloona.Authentication.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;


@Data
@Entity(name = "User_")

@Table(name = "User_", uniqueConstraints = {
        @UniqueConstraint(name = "email_unique", columnNames = "email"),
        @UniqueConstraint(name = "phoneNumber_unique", columnNames = "phoneNumber"),
        @UniqueConstraint(name = "username_unique", columnNames = "username")
})
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User_  implements UserDetails {

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

    @Column(name = "dob", nullable = false)
    private  int dob;
    @Column(name = "age", nullable = false)
    private int age;

    @Enumerated(EnumType.STRING)
    private Role role;


    // every user can have multiple reservations
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Reservation> reservations;



    public void calculateAge(){
        this.age = LocalDate.now().getYear() - this.dob;
    }


    public int getId() {
        return ID;
    }

    public void addReservation(Reservation reservation){
        reservations.add(reservation);

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return role.getAuthorities();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }


}
