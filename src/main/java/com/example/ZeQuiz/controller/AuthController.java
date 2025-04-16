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
import org.springframework.web.server.ResponseStatusException;

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
        // Cek jika username sudah ada
        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body("Username telah ada");
        }

        // Cek apakah password dan konfirmasi password cocok
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("Kata sandi dan konfirmasi kata sandi tidak cocok");
        }

        // Simpan user baru
        User newUser = User.builder()
                .username(request.getUsername())
                .kata_sandi(encoder.encode(request.getPassword())) // Enkripsi password
                .kelas(request.getGrade())
                .build();

        userRepository.save(newUser);

        return ResponseEntity.ok("Registrasi Berhasil");
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestBody request) {
        // Cari user berdasarkan username
        User user = userRepository.findByUsername(request.getUsername()).orElse(null);

        // Jika username tidak ditemukan, kirim error
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponseBody("Username tidak ditemukan", null));
        }

        // Validasi password
        if (!encoder.matches(request.getKata_sandi(), user.getKata_sandi())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponseBody("Kata sandi salah", null));
        }

        // Autentikasi user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getKata_sandi())
        );

        // Ambil detail user
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Generate token JWT
        String token = jwtUtil.generateToken(user.getUsername());

        // Response sukses dengan message dan token
        LoginResponseBody loginResponseBody = LoginResponseBody.builder()
                .message("Login berhasil")
                .token(token)
                .build();

        return ResponseEntity.ok(loginResponseBody);
    }
}
