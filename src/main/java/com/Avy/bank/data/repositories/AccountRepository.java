package com.Avy.bank.data.repositories;

import com.Avy.bank.data.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account,Long> {
    String findByAccountNumber(String accountNumber);
}
