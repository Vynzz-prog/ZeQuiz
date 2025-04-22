package com.example.ZeQuiz.service;

import com.example.ZeQuiz.entity.Soal;
import com.example.ZeQuiz.entity.Topik;
import com.example.ZeQuiz.entity.User;
import com.example.ZeQuiz.repository.SoalRepository;
import com.example.ZeQuiz.repository.TopikRepository;
import com.example.ZeQuiz.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SoalService {

    @Autowired
    private SoalRepository soalRepository;

    @Autowired
    private TopikRepository topikRepository;

    @Autowired
    private UserRepository userRepository;

    public Soal tambahSoal(Long userId, Long topikId, Soal inputSoal) {
        User guru = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Guru tidak ditemukan"));

        Topik topik = topikRepository.findById(topikId)
                .orElseThrow(() -> new RuntimeException("Topik tidak ditemukan"));

        // Cek apakah guru mengakses topik sesuai kelasnya
        if (!topik.getKelas().getId().equals(guru.getKelas().getId())) {
            throw new RuntimeException("Topik bukan milik kelas guru ini");
        }

        inputSoal.setTopik(topik);
        return soalRepository.save(inputSoal);
    }
}
