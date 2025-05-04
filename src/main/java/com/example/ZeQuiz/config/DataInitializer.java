package com.example.ZeQuiz.config;

import com.example.ZeQuiz.entity.Kelas;
import com.example.ZeQuiz.entity.User;
import com.example.ZeQuiz.repository.KelasRepository;
import com.example.ZeQuiz.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner init(UserRepository userRepository, KelasRepository kelasRepository, PasswordEncoder passwordEncoder) {
        return args -> {


            Kelas kelas4 = kelasRepository.findByNama("Kelas 4").orElseGet(() ->
                    kelasRepository.save(Kelas.builder().nama("Kelas 4").build()));

            Kelas kelas5 = kelasRepository.findByNama("Kelas 5").orElseGet(() ->
                    kelasRepository.save(Kelas.builder().nama("Kelas 5").build()));

            Kelas kelas6 = kelasRepository.findByNama("Kelas 6").orElseGet(() ->
                    kelasRepository.save(Kelas.builder().nama("Kelas 6").build()));


            if (!userRepository.existsByUsername("guru1")) {
                User guru1 = User.builder()
                        .username("guru1")
                        .kata_sandi(passwordEncoder.encode("password1"))
                        .kelas(kelas4)
                        .role("GURU")
                        .build();
                userRepository.save(guru1);
                System.out.println("Akun guru1 berhasil dibuat");
            }


            if (!userRepository.existsByUsername("guru2")) {
                User guru2 = User.builder()
                        .username("guru2")
                        .kata_sandi(passwordEncoder.encode("password2"))
                        .kelas(kelas5)
                        .role("GURU")
                        .build();
                userRepository.save(guru2);
                System.out.println("Akun guru2 berhasil dibuat");
            }


            if (!userRepository.existsByUsername("guru3")) {
                User guru3 = User.builder()
                        .username("guru3")
                        .kata_sandi(passwordEncoder.encode("password3"))
                        .kelas(kelas6)
                        .role("GURU")
                        .build();
                userRepository.save(guru3);
                System.out.println("Akun guru3 berhasil dibuat");
            }
        };
    }
}
