package com.example.ZeQuiz.config;

import com.example.ZeQuiz.entity.User;
import com.example.ZeQuiz.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner init(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {

            // Akun guru 1
            if (!userRepository.existsByUsername("guru1")) {
                User guru1 = User.builder()
                        .username("guru1")
                        .kata_sandi(passwordEncoder.encode("password1"))
                        .kelas(4)
                        .role("GURU")
                        .build();
                userRepository.save(guru1);
                System.out.println("✅ Akun guru1 berhasil dibuat");
            }

            // Akun guru 2
            if (!userRepository.existsByUsername("guru2")) {
                User guru2 = User.builder()
                        .username("guru2")
                        .kata_sandi(passwordEncoder.encode("password2"))
                        .kelas(5)
                        .role("GURU")
                        .build();
                userRepository.save(guru2);
                System.out.println("✅ Akun guru2 berhasil dibuat");
            }

            // Guru 3
            if (!userRepository.existsByUsername("guru3")) {
                User guru2 = User.builder()
                        .username("guru3")
                        .kata_sandi(passwordEncoder.encode("password3"))
                        .kelas(6)
                        .role("GURU")
                        .build();
                userRepository.save(guru2);
                System.out.println("✅ Akun guru3 berhasil dibuat");
            }
        };
    }
}

