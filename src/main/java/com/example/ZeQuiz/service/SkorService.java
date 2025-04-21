package com.example.ZeQuiz.service;

import com.example.ZeQuiz.entity.*;
import com.example.ZeQuiz.repository.KuisSoalRepository;
import com.example.ZeQuiz.repository.SkorRepository;
import com.example.ZeQuiz.repository.SoalRepository;
import com.example.ZeQuiz.model.JawabanSiswa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SkorService {

    @Autowired
    private SkorRepository skorRepository;

    @Autowired
    private KuisSoalRepository kuisSoalRepository;

    @Autowired
    private SoalRepository soalRepository;

    public Skor hitungSkor(User user, Kuis kuis, List<JawabanSiswa> jawabanSiswaList) {
        double score = 0;

        int jumlahSoal = kuisSoalRepository.countByKuis(kuis);
        if (jumlahSoal == 0) throw new RuntimeException("Jumlah soal di kuis tidak ditemukan");

        double skorPerSoal = 100.0 / jumlahSoal;

        for (JawabanSiswa jawaban : jawabanSiswaList) {
            Soal soal = soalRepository.findById(jawaban.getSoalId()).orElse(null);
            if (soal != null && soal.getJawabanBenar().equalsIgnoreCase(jawaban.getJawabanDipilih())) {
                score += skorPerSoal;
            }
        }

        Skor skor = new Skor();
        skor.setSiswa(user); // penting: setSiswa (bukan user)
        skor.setKuis(kuis);
        skor.setSkor((int) score);
        skor.setWaktuSelesai(LocalDateTime.now());

        return skorRepository.save(skor);
    }

    public Skor getSkorByUserAndKuis(User user, Kuis kuis) {
        return skorRepository.findBySiswaAndKuis(user, kuis)
                .orElseThrow(() -> new RuntimeException("Skor tidak ditemukan"));
    }

    public List<Skor> getSkorByKuis(Kuis kuis) {
        return skorRepository.findByKuis(kuis);
    }
}
