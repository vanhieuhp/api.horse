package com.horse.api.controller;

import com.horse.business.general.FunctionUtil;
import com.horse.business.service.AccountService;
import com.horse.data.dto.account.AccountRequest;
import com.horse.data.dto.account.AccountResponse;
import com.horse.data.dto.account.PasswordRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping()
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody AccountRequest accountRequest) {

        AccountResponse accountResponse = accountService.createAccount(accountRequest);
        return new ResponseEntity<>(accountResponse, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid AccountRequest accountRequest) {

        String token = accountService.login(accountRequest);
        return new ResponseEntity<>(token, HttpStatus.ACCEPTED);
    }

    @PostMapping("/change_password")
    public ResponseEntity<String> changePassword(@RequestBody @Valid PasswordRequest passwordRequest, HttpServletRequest request) {

        String key = request.getHeader("Authorization");
        String response = accountService.changePassword(passwordRequest.getPassword(), key);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable("id") Integer id) {

        return new ResponseEntity<>(accountService.getOne(id), HttpStatus.OK);
    }

}
