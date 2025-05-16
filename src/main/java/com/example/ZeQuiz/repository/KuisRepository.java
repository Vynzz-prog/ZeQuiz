package com.example.ZeQuiz.repository;

import com.example.ZeQuiz.entity.Kuis;
import com.example.ZeQuiz.entity.Kelas;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface KuisRepository extends JpaRepository<Kuis, Long> {

    // Ambil daftar kuis berdasarkan kelas
    List<Kuis> findByKelas(Kelas kelas);

    // Hitung jumlah kuis berdasarkan kelas (untuk penamaan otomatis)
    long countByKelas(Kelas kelas);
}
