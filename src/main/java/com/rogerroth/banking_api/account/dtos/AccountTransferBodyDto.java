package com.rogerroth.banking_api.account.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountTransferBodyDto {

  private Long fromAccountId;

  private Long toAccountId;

  private BigDecimal amount;
}
