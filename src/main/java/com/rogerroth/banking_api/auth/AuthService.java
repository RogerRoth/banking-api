package com.rogerroth.banking_api.auth;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.rogerroth.banking_api.account.AccountEntity;
import com.rogerroth.banking_api.account.AccountRepository;

@Service
public class AuthService implements UserDetailsService {

  @Autowired
  private AccountRepository accountRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    AccountEntity accountEntity = this.accountRepository.findByEmail(email)
                  .orElseThrow(() -> new UsernameNotFoundException("Account not found"));


    return new org.springframework.security.core.userdetails.User(
      accountEntity.getEmail(),
      accountEntity.getPassword(),
      new ArrayList<>()
    );
  }
  
}
