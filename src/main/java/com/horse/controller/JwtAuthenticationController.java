package com.horse.controller;

import com.horse.business.service.AccountService;
import com.horse.config.exceptionHandler.GeneralExceptionHandler;
import com.horse.config.token.JwtTokenUtil;
import com.horse.data.dto.account.AccountRequest;
import com.horse.data.dto.account.AccountResponse;
import com.horse.data.dto.jwt.AuthenticationRequest;
import com.horse.data.entity.Account;
import com.horse.data.entity.Role;
import com.horse.data.repository.account.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
public class JwtAuthenticationController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody AccountRequest accountRequest) {

        AccountResponse accountResponse = accountService.createAccount(accountRequest);
        return new ResponseEntity<>(accountResponse, HttpStatus.CREATED);
    }

    @PostMapping ("/login")
    public ResponseEntity<?> authenticationRequest(@RequestBody @Valid AuthenticationRequest request,
                                                   HttpServletResponse response) throws IOException {

        // authenticate account
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
            authenticationManager.authenticate(authenticationToken);
        } catch (Exception e) {
            GeneralExceptionHandler.sendErrorUnauthorizedException(response);
            return null;
        }

        // generate token
        List<String> authorities = accountService.getAuthoritiesOfAccount(request.getUsername());

        String accessToken = jwtTokenUtil.generateAccessToken(request.getUsername(), authorities);
        String refreshToken = jwtTokenUtil.generateRefreshToken(request.getUsername());
        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", accessToken);
        tokens.put("refresh_token", refreshToken);
        return new ResponseEntity<>(tokens, HttpStatus.CREATED);
    }

    @GetMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {

        String headerToken = request.getHeader("Authorization");
        String refreshToken = headerToken.substring("Bearer ".length());

        String username = jwtTokenUtil.getSubjectFromToken(refreshToken);

        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new GeneralExceptionHandler("Bad request"));
        List<String> authorities = account.getRoles().stream().map(Role::getCode).collect(Collectors.toList());

        // Generate refresh token
        String accessToken = jwtTokenUtil.generateAccessToken(username, authorities);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", accessToken);
        tokens.put("refresh_token", accessToken);
        return new ResponseEntity<>(tokens, HttpStatus.CREATED);
    }

}
