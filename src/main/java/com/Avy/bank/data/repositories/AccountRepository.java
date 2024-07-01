package com.Avy.bank.data.repositories;

import com.Avy.bank.data.models.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<UserAccount,Long> {


    UserAccount findByAccountNumber(String userAccountNumber);
}
