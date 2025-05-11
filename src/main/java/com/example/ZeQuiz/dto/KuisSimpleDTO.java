package com.example.ZeQuiz.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KuisSimpleDTO {
    private Long id;
    private String nama;
    private Integer timer;
    private Integer jumlahSoal;
    private String tanggal;
    private TopikSimpleDTO topik;
    private KelasSimpleDTO kelas;
}
