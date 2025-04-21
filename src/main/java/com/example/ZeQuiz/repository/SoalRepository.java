package com.example.ZeQuiz.repository;

import com.example.ZeQuiz.entity.Soal;
import com.example.ZeQuiz.entity.Topik;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SoalRepository extends JpaRepository<Soal, Long> {

    // Cari soal berdasarkan topik
    List<Soal> findByTopik(Topik topik);

    // Cari soal secara acak berdasarkan topik
    @Query("SELECT s FROM Soal s WHERE s.topik.id = :topikId ORDER BY FUNCTION('RAND')")
    List<Soal> findRandomSoalByTopikId(@Param("topikId") Long topikId, Pageable pageable);
}
