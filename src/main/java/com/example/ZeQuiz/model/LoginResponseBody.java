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
    private String message;
    private String token;
    private String role;
    private String username;
    private Integer kelas;
}
