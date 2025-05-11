package com.example.ZeQuiz.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KuisResponseDTO {
    private Long id;
    private String namaKuis;
    private Integer timer;
    private Integer jumlahSoal;
    private String tanggal;
    private String namaTopik;
    private String waktuMulai;
    private String waktuSelesai;
}

