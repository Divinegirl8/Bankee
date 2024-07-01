package com.Avy.bank.data.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Setter
@Getter
public class TransactionOnAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private BigDecimal amount;
    private String description;
    private String accountNumber;
    private String accountName;
    private String performedBy;
    @ManyToOne(fetch = FetchType.EAGER)
    private UserAccount account;
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;
    private LocalDateTime performedAt;
}
