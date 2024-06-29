package com.Avy.bank.data.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Setter
@Getter

public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String accountName;
    private String accountNumber;
    private Long balance;
    @Enumerated(EnumType.STRING)
    private AccountType accountType;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    private List<TransactionOnAccount> transactionOnAccountHistory;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
