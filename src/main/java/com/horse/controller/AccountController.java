package com.horse.controller;

import com.horse.business.service.AccountService;
import com.horse.data.dto.account.AccountResponse;
import com.horse.data.dto.account.PasswordRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/change_password")
    public ResponseEntity<String> changePassword(@RequestBody @Valid PasswordRequest passwordRequest) {
        accountService.changePassword(passwordRequest.getPassword());
        return new ResponseEntity<>("Change password successfully!", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(accountService.getOne(id), HttpStatus.OK);
    }

    @GetMapping("/info")
    public ResponseEntity<AccountResponse> getInfo() {
        return new ResponseEntity<>(accountService.getInfo(), HttpStatus.OK);
    }

}
