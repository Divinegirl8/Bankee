package com.Avy.bank.data.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Setter
@Getter

public class UserAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    private String accountName;
    private String accountNumber;
    private BigDecimal balance;
    @Enumerated(EnumType.STRING)
    private AccountType accountType;
    @OneToMany(fetch = FetchType.EAGER)
    private List<TransactionOnAccount> transactionOnAccountHistory;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
