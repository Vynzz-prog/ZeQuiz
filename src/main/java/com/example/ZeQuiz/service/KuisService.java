package com.example.ZeQuiz.service;

import com.example.ZeQuiz.entity.*;
import com.example.ZeQuiz.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KuisService {

    @Autowired
    private KuisRepository kuisRepository;

    @Autowired
    private KuisSoalRepository kuisSoalRepository;

    @Autowired
    private SoalRepository soalRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TopikRepository topikRepository;

    @Autowired
    private KelasRepository kelasRepository;

    public Kuis buatKuis(Long userId, Long topikId, Kuis kuisInput) {
        User guru = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Guru tidak ditemukan"));

        Topik topik = topikRepository.findById(topikId)
                .orElseThrow(() -> new RuntimeException("Topik tidak ditemukan"));

        // Cek agar guru hanya buat kuis di kelasnya
        if (!topik.getKelas().getId().equals(guru.getKelas().getId())) {
            throw new RuntimeException("Topik bukan milik kelas guru");
        }

        // Set data otomatis
        kuisInput.setGuru(guru);
        kuisInput.setKelas(guru.getKelas());
        kuisInput.setTopik(topik);

        // Simpan kuis terlebih dahulu
        Kuis savedKuis = kuisRepository.save(kuisInput);

        // Ambil soal secara acak berdasarkan topik
        List<Soal> soalList = soalRepository.findRandomSoalByTopikId(
                topikId, PageRequest.of(0, kuisInput.getJumlahSoal()));

        // Tambahkan soal ke dalam kuis
        for (Soal soal : soalList) {
            KuisSoal kuisSoal = KuisSoal.builder()
                    .kuis(savedKuis)
                    .soal(soal)
                    .build();
            kuisSoalRepository.save(kuisSoal);
        }

        return savedKuis;
    }

    public List<Kuis> getKuisByKelas(Long kelasId) {
        Kelas kelas = kelasRepository.findById(kelasId)
                .orElseThrow(() -> new RuntimeException("Kelas tidak ditemukan"));
        return kuisRepository.findByKelas(kelas);
    }

    public Kuis findById(Long id) {
        return kuisRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kuis tidak ditemukan"));
    }
}
