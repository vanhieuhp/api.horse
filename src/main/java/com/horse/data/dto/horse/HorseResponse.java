package com.horse.data.dto.horse;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class HorseResponse {

    private Integer id;
    private String name;
    private Date foaled;
    private Integer trainerId;
    private String trainerName;
}
