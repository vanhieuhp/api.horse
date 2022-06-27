package com.horse.config.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.horse.config.exceptionHandler.GeneralExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expirationDateInMs}")
    private int expirationDateInMs;

    @Value("${jwt.refreshExpirationDateInMs}")
    private int refreshExpirationDateInMs;

    public DecodedJWT getDecodedJWTFromToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        return jwtVerifier.verify(token);
    }

    public String getSubjectFromToken(String token) {
        return getDecodedJWTFromToken(token).getSubject();
    }

    public String generateAccessToken(String username, List<String> authorities) {
        return JWT.create().withSubject(username).withClaim("roles", authorities)
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationDateInMs))
                .sign(Algorithm.HMAC256(secret.getBytes()));
    }

    public String generateRefreshToken(String username, List<String> authorities) {
        return JWT.create().withSubject(username).withClaim("roles", authorities)
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshExpirationDateInMs))
                .sign(Algorithm.HMAC256(secret.getBytes()));
    }

    public List<String> getRolesFromToken(String token) {
        return getDecodedJWTFromToken(token).getClaim("roles").asList(String.class);
    }

    public List<SimpleGrantedAuthority> getAuthoritiesFromToken(String token) {
        return getDecodedJWTFromToken(token).getClaim("roles").asList(String.class)
                .stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    public boolean validate(String token, HttpServletResponse response) throws IOException {
        boolean validation = false;
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
            JWTVerifier jwtVerifier = JWT.require(algorithm).build();
            jwtVerifier.verify(token);
            validation = true;
        } catch (Exception e) {
            e.printStackTrace();
            GeneralExceptionHandler.sendErrorTokenException(response, e.getMessage());
        }
        return validation;
    }
}
