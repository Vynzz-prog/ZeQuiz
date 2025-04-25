package com.example.ZeQuiz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SiswaSkorDTO {
    private Long siswaId;
    private String username;
    private Integer skor;           // null jika belum mengerjakan
    private String status;          // "Sudah Mengerjakan" atau "Belum Mengerjakan"
}
