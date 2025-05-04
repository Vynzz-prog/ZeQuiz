package com.example.ZeQuiz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SoalDTO {
    private Long id;
    private String pertanyaan;
    private String gambar;
    private String opsiA;
    private String opsiB;
    private String opsiC;
    private String opsiD;

}
