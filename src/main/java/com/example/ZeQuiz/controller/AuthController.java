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
        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body("Username telah ada");
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("Kata sandi dan konfirmasi kata sandi tidak cocok");
        }

        User newUser = User.builder()
                .username(request.getUsername())
                .kata_sandi(encoder.encode(request.getPassword()))
                .kelas(request.getGrade())
                .role("SISWA") // 👉 Tetapkan role langsung jadi "SISWA"
                .build();

        userRepository.save(newUser);

        return ResponseEntity.ok("Registrasi Berhasil");
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestBody request) {
        User user = userRepository.findByUsername(request.getUsername()).orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponseBody("Username tidak ditemukan", null, null));
        }

        if (!encoder.matches(request.getKata_sandi(), user.getKata_sandi())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponseBody("Kata sandi salah", null, null));
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getKata_sandi())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String token = jwtUtil.generateToken(user.getUsername());

        LoginResponseBody loginResponseBody = LoginResponseBody.builder()
                .message("Login berhasil")
                .token(token)
                .role(user.getRole()) // ✅ Sertakan role dalam response
                .build();

        return ResponseEntity.ok(loginResponseBody);
    }
}
