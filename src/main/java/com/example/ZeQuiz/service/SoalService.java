package com.example.ZeQuiz.service;

import com.example.ZeQuiz.entity.Soal;
import com.example.ZeQuiz.entity.Topik;
import com.example.ZeQuiz.entity.User;
import com.example.ZeQuiz.repository.SoalRepository;
import com.example.ZeQuiz.repository.TopikRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SoalService {

    @Autowired
    private SoalRepository soalRepository;

    @Autowired
    private TopikRepository topikRepository;


    public Soal tambahSoal(User guru, Long topikId, Soal inputSoal) {
        validateGuru(guru);

        Topik topik = topikRepository.findById(topikId)
                .orElseThrow(() -> new RuntimeException("Topik tidak ditemukan"));

        if (!topik.getKelas().getId().equals(guru.getKelas().getId())) {
            throw new RuntimeException("Guru tidak memiliki akses ke topik ini");
        }

        inputSoal.setTopik(topik);
        return soalRepository.save(inputSoal);
    }


    public void hapusSoal(Long soalId, User guru) {
        validateGuru(guru);

        Soal soal = soalRepository.findById(soalId)
                .orElseThrow(() -> new RuntimeException("Soal tidak ditemukan"));

        if (!soal.getTopik().getKelas().getId().equals(guru.getKelas().getId())) {
            throw new RuntimeException("Guru tidak memiliki akses untuk menghapus soal ini");
        }

        soalRepository.delete(soal);
    }


    public List<Soal> getSoalByTopik(Long topikId, User guru) {
        validateGuru(guru);

        Topik topik = topikRepository.findById(topikId)
                .orElseThrow(() -> new RuntimeException("Topik tidak ditemukan"));

        if (!topik.getKelas().getId().equals(guru.getKelas().getId())) {
            throw new RuntimeException("Guru tidak memiliki akses ke topik ini");
        }

        return soalRepository.findByTopik(topik);
    }


    private void validateGuru(User user) {
        if (!"GURU".equalsIgnoreCase(user.getRole())) {
            throw new RuntimeException("Hanya guru yang dapat mengakses fitur ini");
        }
    }
}
