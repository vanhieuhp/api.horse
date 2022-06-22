package com.horse.business.service;

import com.horse.data.dto.account.AccountRequest;
import com.horse.data.dto.account.AccountResponse;

public interface AccountService {

    AccountResponse createAccount(AccountRequest accountRequest);
    String login(AccountRequest accountRequest);
    String changePassword(String password, String key);
    AccountResponse getOne(Integer id);
}
