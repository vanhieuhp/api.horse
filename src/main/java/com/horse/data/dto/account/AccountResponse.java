package com.horse.data.dto.account;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class AccountResponse {

    private Integer id;
    private String username;
    private Integer status;
    private List<String> roles;
    private List<String> trainers;
}
