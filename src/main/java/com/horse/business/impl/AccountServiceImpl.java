package com.horse.business.impl;

import com.horse.business.service.AccountService;
import com.horse.config.exceptionHandler.GeneralExceptionHandler;
import com.horse.data.dto.account.AccountRequest;
import com.horse.data.dto.account.AccountResponse;
import com.horse.data.entity.Account;
import com.horse.data.entity.Role;
import com.horse.data.entity.Trainer;
import com.horse.data.repository.account.AccountRepository;
import com.horse.data.repository.role.RoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    @Transactional
    public AccountResponse createAccount(AccountRequest accountRequest) {

        Optional<Account> account = accountRepository.findByUsername(accountRequest.getUsername());
        if (account.isPresent()) {
           throw new GeneralExceptionHandler("Username is existed");
        }
        // save account
        Account accountEntity = new Account();
        accountEntity.setUsername(accountRequest.getUsername());
        accountEntity.setStatus(1);
        accountEntity.setPassword(passwordEncoder.encode(accountRequest.getPassword()));

        // set role for account
        Set<Role> roles = new HashSet<>();
        accountRequest.getRoles().forEach(roleRequest -> {
            Role role = roleRepository.findRoleByCode(roleRequest.getCode())
                    .orElseThrow(()-> new GeneralExceptionHandler("Role not found with code: " + roleRequest.getCode()));
            roles.add(role);
        });
        accountEntity.setRoles(roles);

        Account newAccount = accountRepository.save(accountEntity);

        return transferAccount(newAccount);
    }

    @Override
    @Transactional
    public void changePassword(String password) {
        Account account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        account.setPassword(passwordEncoder.encode(password));
        accountRepository.save(account);
    }

    @Override
    public AccountResponse getOne(Integer id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new GeneralExceptionHandler("Account not found with id: " + id));

        return transferAccount(account);
    }

    @Override
    public AccountResponse getInfo() {
        Account account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return transferAccount(account);
    }

    @Override
    public List<String> getAuthoritiesOfAccount(String username) {
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new GeneralExceptionHandler("User not found with username: " + username));

        return account.getRoles().stream().map(Role::getCode).collect(Collectors.toList());
    }

    public AccountResponse transferAccount(Account account) {

        AccountResponse accountResponse = mapper.map(account, AccountResponse.class);

        // set trainers and roles for account
        List<String> trainers = account.getTrainers().stream().map(Trainer::getName).collect(Collectors.toList());
        List<String> roles = account.getRoles().stream().map(Role::getName).collect(Collectors.toList());

        accountResponse.setTrainers(trainers);
        accountResponse.setRoles(roles);

        return accountResponse;
    }
}
