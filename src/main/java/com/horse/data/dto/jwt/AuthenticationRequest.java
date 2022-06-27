package com.horse.data.dto.jwt;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class AuthenticationRequest {

    @NotBlank(message = "username should not be blank")
    private String username;

    @Size(min = 6, message = "password should equal or higher than 6 characters")
    private String password;
}
