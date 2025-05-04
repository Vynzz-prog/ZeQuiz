package com.example.ZeQuiz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SiswaSkorDTO {
    private Long siswaId;
    private String username;
    private Integer skor;
    private String status;
}
