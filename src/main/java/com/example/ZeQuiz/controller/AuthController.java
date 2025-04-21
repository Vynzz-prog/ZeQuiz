package com.example.ZeQuiz.controller;

import com.example.ZeQuiz.entity.User;
import com.example.ZeQuiz.model.LoginRequestBody;
import com.example.ZeQuiz.model.LoginResponseBody;
import com.example.ZeQuiz.model.RegisterRequest;
import com.example.ZeQuiz.repository.UserRepository;
import com.example.ZeQuiz.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("zequiz/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtil jwtUtil;

    @PostMapping(path = "/register")
    public ResponseEntity<?> registerNewUser(@RequestBody RegisterRequest request) {
        // Validasi apakah username sudah ada
        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("message", "Username telah ada"));
        }

        // Validasi apakah password dan konfirmasi password cocok
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("message", "Kata sandi dan konfirmasi kata sandi tidak cocok"));
        }

        // Validasi apakah kelas berada di antara 4 dan 6
        if (request.getGrade() < 4 || request.getGrade() > 6) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("message", "Kelas yang dapat mendaftar hanya kelas 4, 5, dan 6"));
        }

        // Buat pengguna baru
        User newUser = User.builder()
                .username(request.getUsername())
                .kata_sandi(encoder.encode(request.getPassword()))
                .kelas(request.getGrade())
                .role(request.getRole() != null ? request.getRole() : "SISWA") // Role default "SISWA"
                .build();

        // Simpan pengguna ke database
        userRepository.save(newUser);

        // Berikan respons sukses
        return ResponseEntity.ok(Map.of("message", "Registrasi berhasil"));
    }


    @PostMapping(path = "/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestBody request) {
        User user = userRepository.findByUsername(request.getUsername()).orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    LoginResponseBody.builder()  // Gunakan builder
                            .message("Username tidak ditemukan")
                            .build());  // Hanya mengisi message dan membiarkan parameter lain null
        }

        if (!encoder.matches(request.getKata_sandi(), user.getKata_sandi())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    LoginResponseBody.builder()  // Gunakan builder
                            .message("Kata sandi salah")
                            .build());  // Hanya mengisi message dan membiarkan parameter lain null
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getKata_sandi())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String token = jwtUtil.generateToken(user.getUsername());

        LoginResponseBody loginResponseBody = LoginResponseBody.builder()
                .message("Login berhasil")
                .token(token)
                .role(user.getRole())
                .username(user.getUsername())
                .kelas(user.getKelas())
                .build();

        return ResponseEntity.ok(loginResponseBody);
    }


    @PostMapping(path = "/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok(Map.of("message", "Logout berhasil. Silakan hapus token di sisi klien."));
    }
}