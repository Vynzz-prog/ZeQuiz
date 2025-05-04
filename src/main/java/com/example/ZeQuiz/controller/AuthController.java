package com.example.ZeQuiz.controller;

import com.example.ZeQuiz.entity.Kelas;
import com.example.ZeQuiz.entity.User;
import com.example.ZeQuiz.model.LoginRequestBody;
import com.example.ZeQuiz.model.LoginResponseBody;
import com.example.ZeQuiz.model.RegisterRequest;
import com.example.ZeQuiz.repository.KelasRepository;
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
    private KelasRepository kelasRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtil jwtUtil;

    @PostMapping(path = "/register")
    public ResponseEntity<?> registerNewUser(@RequestBody RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("pesan", "Username telah ada"));
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("pesan", "Kata sandi dan konfirmasi kata sandi tidak cocok"));
        }

        if (request.getGrade() < 4 || request.getGrade() > 6) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("pesan", "Kelas yang dapat mendaftar hanya kelas 4, 5, dan 6"));
        }

        Kelas kelas = kelasRepository.findByNama("Kelas " + request.getGrade())
                .orElseThrow(() -> new RuntimeException("Kelas tidak ditemukan di database"));

        User newUser = User.builder()
                .username(request.getUsername())
                .kata_sandi(encoder.encode(request.getPassword()))
                .kelas(kelas)
                .role(request.getRole() != null ? request.getRole() : "SISWA")
                .build();

        userRepository.save(newUser);
        return ResponseEntity.ok(Map.of("pesan", "Registrasi berhasil"));
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestBody request) {
        User user = userRepository.findByUsername(request.getUsername()).orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    Map.of("pesan", "Username tidak ditemukan")
            );
        }

        if (!encoder.matches(request.getKata_sandi(), user.getKata_sandi())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    Map.of("pesan", "Kata sandi salah")
            );
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getKata_sandi())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtUtil.generateToken(user.getUsername());

        LoginResponseBody loginResponseBody = LoginResponseBody.builder()
                .id(user.getId())
                .pesan("Login berhasil")
                .token(token)
                .role(user.getRole())
                .username(user.getUsername())
                .kelas(user.getKelas().getNama())
                .kelasId(user.getKelas().getId())
                .build();

        return ResponseEntity.ok(loginResponseBody);
    }

    @PostMapping(path = "/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok(Map.of("pesan", "Logout berhasil. Silakan hapus token di sisi klien."));
    }
}
