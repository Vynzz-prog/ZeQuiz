package com.example.ZeQuiz.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseBody {
    private Long id;
    private String pesan;
    private String token;
    private String role;
    private String username;
    private String kelas;
    private Long kelasId;

}
