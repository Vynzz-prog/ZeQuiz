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
    private String kelas; // ganti dari Integer → String karena akan mengembalikan nama kelas, seperti "Kelas 4"
}
