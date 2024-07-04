package com.Avy.bank.data.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Setter
@Getter


public class UserAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String accountName;
    private Long userId;
    private String accountEmail;
    private String accountPassword;
    private boolean isLogin;
    private String accountNumber;
    private BigDecimal balance;
    @Enumerated(EnumType.STRING)
    private AccountType accountType;
    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.MERGE)
    private List<TransactionOnAccount> transactionOnAccountHistory;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
