package com.example.ZeQuiz.repository;

import com.example.ZeQuiz.entity.KuisSoal;
import com.example.ZeQuiz.entity.Kuis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KuisSoalRepository extends JpaRepository<KuisSoal, Long> {
    List<KuisSoal> findByKuis(Kuis kuis);

    int countByKuis(Kuis kuis);  //hitung jumlah soal dalam 1 kuis
}
