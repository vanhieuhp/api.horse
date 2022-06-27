package com.horse.data.dto.account;

import com.horse.data.dto.RoleRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
public class AccountRequest {

    @NotBlank(message = "Username should not be blank")
    private String username;

    @Size(min = 6, message = "Password should equal or than 6 characters")
    private String password;

    @NotNull(message = "Roles should not be null")
    private Set<RoleRequest> roles;
}
