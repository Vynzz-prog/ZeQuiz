package com.example.ZeQuiz.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KuisSimpleDTO {
    private Long id;
    private Integer timer;
    private Integer jumlahSoal;
    private String tanggal;
    private TopikSimpleDTO topik;
    private KelasSimpleDTO kelas;
}
