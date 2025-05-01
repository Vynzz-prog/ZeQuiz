package com.example.ZeQuiz.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SkorResponseDTO {
    private Long id;
    private KuisSimpleDTO kuis;
    private Long siswaId;
    private Integer skor;
}