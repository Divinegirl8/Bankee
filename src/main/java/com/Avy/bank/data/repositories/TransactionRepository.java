package com.Avy.bank.data.repositories;

import com.Avy.bank.data.models.TransactionOnAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionOnAccount, Long> {
}
