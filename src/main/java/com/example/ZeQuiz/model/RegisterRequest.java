package com.example.ZeQuiz.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String username;
    private Integer kelas;
    private String kata_sandi;
    private String konfirmasi_kata_sandi; // Tambahan untuk konfirmasi password
    private String role; //  GURU atau SISWA

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return kata_sandi;
    }

    public String getConfirmPassword() {
        return konfirmasi_kata_sandi;
    }

    public Integer getGrade() {
        return kelas;
    }

    public String getRole() {
        return role;
    }
}
