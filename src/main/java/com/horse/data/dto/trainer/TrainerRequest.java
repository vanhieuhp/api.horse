package com.horse.data.dto.trainer;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class TrainerRequest {

    @NotBlank(message = "name should not be blank")
    private String name;

    @Min(18)
    @Max(60)
    @NotNull
    private Integer age;

    @NotBlank(message = "gender should not be blank")
    private String gender;

    @NotBlank(message = "address should not be blank")
    private String address;
}
