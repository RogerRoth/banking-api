package com.rogerroth.banking_api.account;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.stream.IntStream;
import java.util.List;
import java.util.ArrayList;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {
  @Autowired
  private MockMvc mockMvc;
  
  @Autowired
  private AccountRepository accountRepository;

  private List<Long> accountIds;

  @BeforeEach
  void setup() {
    accountRepository.deleteAll();

    accountIds = new ArrayList<>();

    IntStream.range(1, 11).forEach(i -> {
      AccountEntity accountEntity = new AccountEntity();
      accountEntity.setBalance(BigDecimal.valueOf(1000 + i*10));
      AccountEntity acc = accountRepository.save(accountEntity);

      System.out.println("#########");
      System.out.println(acc);
      System.out.println("#########");

      accountIds.add(acc.getId());
    });
  }

  @Test
    void testGetAccountById() throws Exception {
      Long accountId = accountIds.get(0);

      System.out.println(mockMvc.perform(MockMvcRequestBuilders.get("/accounts/"+accountId)));

      mockMvc.perform(MockMvcRequestBuilders.get("/accounts/"+accountId)
              .accept(MediaType.APPLICATION_JSON))
              .andExpect(status().isOk())
              .andExpect(jsonPath("$.balance").exists());  // Verifica o saldo da conta com ID 1
    }

    @Test
    void testDebitAccount() throws Exception {
      Long accountId = accountIds.get(0);

      mockMvc.perform(MockMvcRequestBuilders.put("/accounts/"+accountId+"/debit")
              .contentType(MediaType.APPLICATION_JSON)
              .content("{\"amount\": 50.0}"))
              .andExpect(status().isOk());
    }

    @Test
    void testTransferBetweenAccounts() throws Exception {
      Long fromAccountId = accountIds.get(0);
      Long toAccountId = accountIds.get(1);
        mockMvc.perform(MockMvcRequestBuilders.post("/accounts/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"fromAccountId\": "+fromAccountId+", \"toAccountId\": "+toAccountId+", \"amount\": 100.0}"))
                .andExpect(status().isOk());

        // Verificar o saldo da conta 1 após a transferência
        mockMvc.perform(MockMvcRequestBuilders.get("/accounts/"+fromAccountId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance", is(910.0)));  // Saldo atualizado da conta 1

        // Verificar o saldo da conta 2 após a transferência
        mockMvc.perform(MockMvcRequestBuilders.get("/accounts/"+toAccountId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance", is(1130.0)));  // Saldo atualizado da conta 2
    }
}
