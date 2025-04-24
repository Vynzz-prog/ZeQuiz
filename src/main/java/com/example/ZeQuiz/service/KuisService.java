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

        if (!"GURU".equalsIgnoreCase(guru.getRole())) {
            throw new RuntimeException("Hanya guru yang dapat membuat kuis");
        }

        Topik topik = topikRepository.findById(topikId)
                .orElseThrow(() -> new RuntimeException("Topik tidak ditemukan"));

        if (!topik.getKelas().getId().equals(guru.getKelas().getId())) {
            throw new RuntimeException("Topik bukan milik kelas guru");
        }

        List<Soal> soalList = soalRepository.findRandomSoalByTopikId(
                topikId, PageRequest.of(0, kuisInput.getJumlahSoal()));

        if (soalList.size() < kuisInput.getJumlahSoal()) {
            throw new RuntimeException("Jumlah soal pada topik tidak mencukupi");
        }

        kuisInput.setGuru(guru);
        kuisInput.setKelas(guru.getKelas());
        kuisInput.setTopik(topik);

        Kuis savedKuis = kuisRepository.save(kuisInput);

        for (Soal soal : soalList) {
            KuisSoal kuisSoal = KuisSoal.builder()
                    .kuis(savedKuis)
                    .soal(soal)
                    .build();
            kuisSoalRepository.save(kuisSoal);
        }

        System.out.println("✅ Guru " + guru.getUsername() + " membuat kuis untuk topik: " + topik.getNama());

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
