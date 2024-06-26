package com.Avy.bank.data.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long amount;
    private String description;
    @ManyToOne(fetch = FetchType.EAGER)
    private Account account;
    private TransactionType transactionType;
    private TransactionStatus status;
}
