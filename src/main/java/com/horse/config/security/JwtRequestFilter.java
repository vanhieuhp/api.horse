package com.horse.config.security;

import com.horse.config.exceptionHandler.GeneralExceptionHandler;
import com.horse.config.token.JwtTokenUtil;
import com.horse.data.entity.Account;
import com.horse.data.repository.account.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.logging.log4j.util.Strings.isEmpty;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String currentUrl = request.getServletPath();
        if (currentUrl.equals("/api/login") || currentUrl.equals("/api/signup")){
            filterChain.doFilter(request, response);
        } else {
            String bearerToken = request.getHeader(AUTHORIZATION);

            if (isEmpty(bearerToken) || !bearerToken.startsWith("Bearer ")) {
                GeneralExceptionHandler.sendErrorUnauthorizedException(response);
                filterChain.doFilter(request, response);
                return;
            }

            // get jwt token and validate
            final String token = bearerToken.split(" ")[1].trim();
            if (!jwtTokenUtil.validate(token, response)) {
                filterChain.doFilter(request, response);
                return;
            }

            // get user identity and set it on the spring security context
            Account account = accountRepository.findByUsername(jwtTokenUtil.getSubjectFromToken(token)).orElse(null);

            List<SimpleGrantedAuthority> authorities = null;
            if (account != null) {
                authorities = account.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getCode())).collect(Collectors.toList());
            }

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(account, null,authorities);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        }
    }
}
