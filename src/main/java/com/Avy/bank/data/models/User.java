package com.Avy.bank.data.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Setter
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullName;
    private String email;
    private String password;
    private String address;
    private String phoneNumber;
    private AccountType accountType;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    private List<Account> account;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
