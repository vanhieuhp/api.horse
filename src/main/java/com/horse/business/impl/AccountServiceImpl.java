package com.horse.business.impl;

import com.horse.config.exceptionHandler.GeneralExceptionHandler;
import com.horse.data.dto.account.AccountRequest;
import com.horse.data.dto.account.AccountResponse;
import com.horse.data.entity.Account;
import com.horse.data.entity.Trainer;
import com.horse.data.repository.AccountRepository;
import com.horse.config.security.PasswordGenerator;
import com.horse.config.security.SingletonClass;
import com.horse.business.service.AccountService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    @Transactional
    public AccountResponse createAccount(AccountRequest accountRequest) {

        Optional<Account> account = accountRepository.findByUsername(accountRequest.getUsername());
        account.ifPresent( s -> {throw new GeneralExceptionHandler("Username is existed"); });

        // save account
        Account accountEntity = new Account();
        accountEntity.setUsername(accountRequest.getUsername());
        accountEntity.setStatus(1);
        accountEntity.setPassword(PasswordGenerator.generatorPassword(accountRequest.getPassword()));
        accountRepository.save(accountEntity);

        // set a response account and return it
        AccountResponse accountResponse = mapper.map(accountEntity, AccountResponse.class);

        return accountResponse;
    }

    @Override
    public String login(AccountRequest accountRequest) {

        Account account = accountRepository.findByUsername(accountRequest.getUsername())
                .orElseThrow(() -> new GeneralExceptionHandler("Username is not existed"));

        // check password to match
        if (PasswordGenerator.checkPasswordInMD5(accountRequest.getPassword(), account.getPassword())) {

            if (SingletonClass.getDbObject().containsValue(account.getId())) {
                return null;
            } else {
                String token = PasswordGenerator.generateString();
                // save key and account_id to singleton class
                SingletonClass.setDbObject(token, account.getId());
                return token;
            }
        }
        return "Login failure!";
    }

    @Override
    @Transactional
    public String changePassword(String password, String key) {

        Integer id = SingletonClass.getDbObject().get(key);

        if (id != null) {

            // Convert password to md5
            String newPasswordMD5 = PasswordGenerator.generatorPassword(password);

            Account account = accountRepository.findById(id).get();
            account.setPassword(newPasswordMD5);
            accountRepository.save(account);

            return "Change Password Successfully!";
        } else {
            return "Change Password failure!";
        }
    }

    @Override
    public AccountResponse getOne(Integer id) {

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new GeneralExceptionHandler("Account not found with id: " + id));

        AccountResponse accountResponse = mapper.map(account, AccountResponse.class);

        // set trainer list for account
        List<String> trainerList = account.getTrainers().stream().map(Trainer::getName).collect(Collectors.toList());
        accountResponse.setTrainers(trainerList);

        return accountResponse;
    }
}
