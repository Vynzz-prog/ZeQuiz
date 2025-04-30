package com.example.ZeQuiz.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KelasSimpleDTO {
    private Long id;
    private String nama;
}