package com.rogerroth.banking_api.account.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountTransferBodyDto {

  @NotBlank(message = "FromAccountId is required")
  private Long fromAccountId;
  
  @NotBlank(message = "toAccountId is required")
  private Long toAccountId;
  
  @NotBlank(message = "amount is required")
  private BigDecimal amount;
}
