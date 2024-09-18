package com.rogerroth.banking_api.account;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import java.util.Optional;
import jakarta.transaction.Transactional;

@Service
public class AccountService {
  
  private final AccountRepository accountRepository;

  public AccountService(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  public AccountEntity getById(Long accountId) {
    Optional<AccountEntity> accountEntity = this.accountRepository.findById(accountId);

    if(!accountEntity.isPresent()) {
      throw new RuntimeException("Account not found");
    }

    return accountEntity.get();
  
  }


  @Transactional
  public void debit(Long accountId, BigDecimal amount) {
    AccountEntity accountEntity = this.accountRepository.findByIdForUpdate(accountId)
      .orElseThrow(() -> new RuntimeException("Account not found"));

    if (accountEntity.getBalance().compareTo(amount) < 0) {
      throw new RuntimeException("Insufficient balance");
    }

    accountEntity.setBalance(accountEntity.getBalance().subtract(amount));
    this.accountRepository.save(accountEntity);
  }

  @Transactional
  public void transfer(Long fromAccountId, Long toAccountId, BigDecimal amount) {
    AccountEntity fromAccountEntity = this.accountRepository.findByIdForUpdate(fromAccountId)
      .orElseThrow(() -> new RuntimeException("Origin account not found"));

    AccountEntity toAccountEntity = this.accountRepository.findByIdForUpdate(toAccountId)
    .orElseThrow(() -> new RuntimeException("Target account not found"));

    if (fromAccountEntity.getBalance().compareTo(amount) < 0){
      throw new RuntimeException("Insufficient balance in the origin account");
    }

    fromAccountEntity.setBalance(fromAccountEntity.getBalance().subtract(amount));
    toAccountEntity.setBalance(toAccountEntity.getBalance().add(amount));

    this.accountRepository.save(fromAccountEntity);
    this.accountRepository.save(toAccountEntity);
  }
}
