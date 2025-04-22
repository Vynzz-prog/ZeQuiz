package com.example.ZeQuiz.repository;

import com.example.ZeQuiz.entity.Kelas;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface KelasRepository extends JpaRepository<Kelas, Long> {
    // Menambahkan metode untuk mencari Kelas berdasarkan nama
    Optional<Kelas> findByNama(String nama);
}
