package com.horse.data.dto.trainer;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class TrainerResponse {

    private Integer id;
    private String name;
    private Integer accountId;
    private String accountName;
    private List<String> horses;
}
