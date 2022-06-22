package com.horse.data.dto.horse;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class HorseRequest {

    @NotBlank(message = "Name should not be blank")
    private String name;

    @NotNull(message = "Foaled should not be null")
    private Date foaled;

    @NotNull(message = "TrainerID should not be null")
    private Integer trainerId;

    private Integer price;
}
