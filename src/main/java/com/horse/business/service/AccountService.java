package com.horse.business.service;

import com.horse.data.dto.account.AccountRequest;
import com.horse.data.dto.account.AccountResponse;

import java.util.List;

public interface AccountService {

    AccountResponse createAccount(AccountRequest accountRequest);
    void changePassword(String password);
    AccountResponse getOne(Integer id);
    AccountResponse getInfo();
    List<String> getAuthoritiesOfAccount(String username);
}
