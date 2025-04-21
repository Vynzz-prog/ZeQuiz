package com.example.ZeQuiz.repository;

import com.example.ZeQuiz.entity.Topik;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ZeQuiz.entity.Kelas;

import java.util.List;

public interface TopikRepository extends JpaRepository<Topik, Long> {
    List<Topik> findByKelas(Kelas kelas);

}
