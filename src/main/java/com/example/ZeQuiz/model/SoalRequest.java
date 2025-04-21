package com.example.ZeQuiz.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SoalRequest {
    private String pertanyaan;
    private String opsiA;
    private String opsiB;
    private String opsiC;
    private String opsiD;
    private String jawabanBenar;
    private Long topikId;
}
