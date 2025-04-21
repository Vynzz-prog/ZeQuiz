package com.example.ZeQuiz.service;

import com.example.ZeQuiz.entity.*;
import com.example.ZeQuiz.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;

@Service
public class KuisService {

    @Autowired
    private KuisRepository kuisRepository;

    @Autowired
    private SoalRepository soalRepository;

    @Autowired
    private KuisSoalRepository kuisSoalRepository;

    @Autowired
    private KelasRepository kelasRepository; // Tambahan untuk ambil kelas

    /**
     * Membuat kuis baru dan menambahkan soal berdasarkan topik yang dipilih.
     */
    public Kuis buatKuis(Kuis kuis, Long topikId) {
        // Simpan kuis terlebih dahulu
        Kuis savedKuis = kuisRepository.save(kuis);

        // Ambil soal secara acak berdasarkan topik
        List<Soal> soalList = soalRepository.findRandomSoalByTopikId(topikId, PageRequest.of(0, kuis.getJumlahSoal()));

        // Menambahkan soal ke dalam kuis
        for (Soal soal : soalList) {
            KuisSoal kuisSoal = KuisSoal.builder()
                    .kuis(savedKuis)
                    .soal(soal)
                    .build();
            kuisSoalRepository.save(kuisSoal);
        }

        return savedKuis;
    }

    /**
     * Mengambil kuis berdasarkan ID.
     */
    public Optional<Kuis> findById(Long id) {
        return kuisRepository.findById(id);
    }

    /**
     * Mengambil semua kuis yang ada.
     */
    public List<Kuis> findAll() {
        return kuisRepository.findAll();
    }

    /**
     * Mengambil kuis berdasarkan kelas.
     */
    public List<Kuis> getKuisByKelas(Long kelasId) {
        Kelas kelas = kelasRepository.findById(kelasId)
                .orElseThrow(() -> new RuntimeException("Kelas tidak ditemukan"));

        return kuisRepository.findByKelas(kelas);
    }
}
