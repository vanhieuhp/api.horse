package com.horse.data.dto.account;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class PasswordRequest {

    @NotBlank
    @Size(min = 6)
    private String password;
}
