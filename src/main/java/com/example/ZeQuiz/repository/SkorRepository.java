package com.example.ZeQuiz.repository;

import com.example.ZeQuiz.entity.Skor;
import com.example.ZeQuiz.entity.Kuis;
import com.example.ZeQuiz.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SkorRepository extends JpaRepository<Skor, Long> {

    List<Skor> findBySiswa(User siswa);

    List<Skor> findByKuis(Kuis kuis);

    Optional<Skor> findBySiswaAndKuis(User siswa, Kuis kuis);
}
