package com.example.ZeQuiz.dto;

import lombok.Data;

@Data
public class KuisDTO {
    private Long id;
    private Integer timer;
    private Integer jumlahSoal;
    private String tanggal;
    private String namaTopik;
}

