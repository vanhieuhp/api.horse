package com.horse.data.dto.trainer;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class TrainerRequest {

    @NotBlank(message = "Name should not be blank")
    private String name;

    @NotNull(message = "AccountID should not be null")
    private Integer accountId;

}
