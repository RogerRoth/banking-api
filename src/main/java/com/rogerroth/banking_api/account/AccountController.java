package com.rogerroth.banking_api.account;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rogerroth.banking_api.account.dtos.AccountTransferBodyDto;

import java.math.BigDecimal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/accounts")
public class AccountController {
  
  private final AccountService accountService;

  public AccountController (AccountService accountService) {
    this.accountService = accountService;
  }

  @GetMapping("/{accountId}")
  public ResponseEntity<AccountEntity> getById(@PathVariable Long accountId) {
    AccountEntity accountEntity = this.accountService.getById(accountId);

    return ResponseEntity.status(200).body(accountEntity);
  }
  

  @PostMapping("/{accountId}/debit")
  public ResponseEntity<?> debit(@PathVariable Long accountId, @RequestParam BigDecimal amount) {
      this.accountService.debit(accountId, amount);
      
      return ResponseEntity.ok().build();
  }

  @PostMapping("/transfer")
  public ResponseEntity<?> transfer(
    @RequestBody AccountTransferBodyDto accountTransferBodyDto
    // @RequestParam Long fromAccountId, 
    // @RequestParam Long toAccountId, 
    // @RequestParam BigDecimal amount
  ) {
      this.accountService.transfer(
        accountTransferBodyDto.getFromAccountId(), 
        accountTransferBodyDto.getToAccountId(),
        accountTransferBodyDto.getAmount()
        );
      
      return ResponseEntity.ok().build();
  }
  
  
}
