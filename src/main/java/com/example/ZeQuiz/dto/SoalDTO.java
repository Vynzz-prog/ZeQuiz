package com.example.ZeQuiz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SoalDTO {
    private Long id;
    private String pertanyaan;   // null jika soal bergambar
    private String gambar;       // nama file, null jika tidak ada
    private String opsiA;
    private String opsiB;
    private String opsiC;
    private String opsiD;
    // jangan sertakan jawabanBenar
}
