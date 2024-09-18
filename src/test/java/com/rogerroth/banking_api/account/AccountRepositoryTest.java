package com.rogerroth.banking_api.account;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.stream.IntStream;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@Rollback(false)
public class AccountRepositoryTest {
  
  @Autowired
  private AccountRepository accountRepository;

  private List<Long> accountIds;

  @BeforeEach
  void setup() {
    accountRepository.deleteAll();

    accountIds = new ArrayList<>();
    
    IntStream.range(1, 2).forEach(i -> {
      AccountEntity accountEntity = new AccountEntity();
      accountEntity.setBalance(BigDecimal.valueOf(1000 + i*10));
      AccountEntity aaa = accountRepository.save(accountEntity);

      
      accountIds.add(aaa.getId());
    });
  }

  @Test
  void testFindByIdForUpdate() {
    Long accountId = accountIds.get(0);
    Optional<AccountEntity> accountEntity = accountRepository.findByIdForUpdate(accountId);

    assertTrue(accountEntity.isPresent());
  }
}
