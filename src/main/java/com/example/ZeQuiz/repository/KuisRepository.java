package com.example.ZeQuiz.repository;

import com.example.ZeQuiz.entity.Kuis;
import com.example.ZeQuiz.entity.Kelas;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface KuisRepository extends JpaRepository<Kuis, Long> {
    List<Kuis> findByKelas(Kelas kelas);
}
