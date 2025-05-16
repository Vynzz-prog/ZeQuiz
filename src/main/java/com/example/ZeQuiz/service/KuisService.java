package com.example.ZeQuiz.service;

import com.example.ZeQuiz.entity.*;
import com.example.ZeQuiz.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * Guru membuat kuis baru berdasarkan topik.
     */
    public Kuis buatKuis(Long userId, Long topikId, Kuis kuisInput) {
        // Cari guru
        User guru = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Guru tidak ditemukan"));

        if (!"GURU".equalsIgnoreCase(guru.getRole())) {
            throw new RuntimeException("Hanya guru yang dapat membuat kuis");
        }

        // Cari topik
        Topik topik = topikRepository.findById(topikId)
                .orElseThrow(() -> new RuntimeException("Topik tidak ditemukan"));

        if (!topik.getKelas().getId().equals(guru.getKelas().getId())) {
            throw new RuntimeException("Topik bukan milik kelas guru");
        }

        // Ambil soal dari topik
        List<Soal> soalList = soalRepository.findRandomSoalByTopikId(
                topikId, PageRequest.of(0, kuisInput.getJumlahSoal()));

        if (soalList.size() < kuisInput.getJumlahSoal()) {
            throw new RuntimeException("Jumlah soal pada topik tidak mencukupi");
        }

        // Validasi waktuMulai dan waktuSelesai (jika diisi)
        if (kuisInput.getWaktuMulai() != null && kuisInput.getWaktuSelesai() != null) {
            if (kuisInput.getWaktuMulai().isAfter(kuisInput.getWaktuSelesai())) {
                throw new RuntimeException("Waktu mulai tidak boleh setelah waktu selesai.");
            }
        }

        // Generate nama kuis: "Kuis ke - X"
        long jumlahKuisSebelumnya = kuisRepository.countByKelas(guru.getKelas());
        String namaKuisBaru = "Kuis ke - " + (jumlahKuisSebelumnya + 1);
        kuisInput.setNama(namaKuisBaru);

        // Set data lainnya
        kuisInput.setGuru(guru);
        kuisInput.setKelas(guru.getKelas());
        kuisInput.setTopik(topik);
        kuisInput.setTanggal(LocalDate.now());

        // Simpan kuis
        Kuis savedKuis = kuisRepository.save(kuisInput);

        // Simpan relasi kuis-soal
        for (Soal soal : soalList) {
            KuisSoal kuisSoal = KuisSoal.builder()
                    .kuis(savedKuis)
                    .soal(soal)
                    .build();
            kuisSoalRepository.save(kuisSoal);
        }

        System.out.println("✅ Guru " + guru.getUsername() + " membuat " + savedKuis.getNama());

        return savedKuis;
    }

    /**
     * Daftar kuis berdasarkan kelas.
     */
    public List<Kuis> getKuisByKelas(Long kelasId) {
        Kelas kelas = kelasRepository.findById(kelasId)
                .orElseThrow(() -> new RuntimeException("Kelas tidak ditemukan"));
        return kuisRepository.findByKelas(kelas);
    }

    /**
     * Cari kuis berdasarkan ID.
     */
    public Kuis findById(Long id) {
        return kuisRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kuis tidak ditemukan"));
    }

    /**
     * Ambil semua soal dari kuis.
     */
    public List<Soal> getSoalDariKuis(Long kuisId) {
        Kuis kuis = findById(kuisId);
        List<KuisSoal> kuisSoalList = kuisSoalRepository.findByKuis(kuis);

        List<Soal> soalList = kuisSoalList.stream()
                .map(KuisSoal::getSoal)
                .collect(Collectors.toList());

        // ✅ Acak urutan soal sebelum dikirim ke frontend
        Collections.shuffle(soalList);

        return soalList;
    }

}
