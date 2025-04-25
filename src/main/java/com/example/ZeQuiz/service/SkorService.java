package com.example.ZeQuiz.service;

import com.example.ZeQuiz.dto.SiswaSkorDTO;
import com.example.ZeQuiz.entity.*;
import com.example.ZeQuiz.model.JawabanSiswa;
import com.example.ZeQuiz.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SkorService {

    @Autowired
    private SkorRepository skorRepository;

    @Autowired
    private KuisSoalRepository kuisSoalRepository;

    @Autowired
    private SoalRepository soalRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Hitung skor akhir siswa berdasarkan jawaban.
     */
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
        skor.setSiswa(user);
        skor.setKuis(kuis);
        skor.setSkor((int) score);
        skor.setWaktuSelesai(LocalDateTime.now());

        return skorRepository.save(skor);
    }

    /**
     * Ambil skor berdasarkan user dan kuis.
     */
    public Skor getSkorByUserAndKuis(User user, Kuis kuis) {
        return skorRepository.findBySiswaAndKuis(user, kuis)
                .orElseThrow(() -> new RuntimeException("Skor tidak ditemukan"));
    }

    /**
     * Ambil semua skor yang terkait dengan kuis.
     */
    public List<Skor> getSkorByKuis(Kuis kuis) {
        return skorRepository.findByKuis(kuis);
    }

    /**
     * Mendapatkan status pengerjaan kuis oleh siswa dalam satu kelas dengan format DTO.
     */
    public List<SiswaSkorDTO> getStatusPengerjaanKuis(Kuis kuis, List<User> siswaList) {
        List<Skor> skorList = skorRepository.findByKuis(kuis);
        Map<Long, Skor> skorMap = skorList.stream()
                .collect(Collectors.toMap(s -> s.getSiswa().getId(), s -> s));

        List<SiswaSkorDTO> hasil = new ArrayList<>();

        for (User siswa : siswaList) {
            Skor skor = skorMap.get(siswa.getId());
            if (skor != null) {
                hasil.add(new SiswaSkorDTO(siswa.getId(), siswa.getUsername(), skor.getSkor(), "Sudah mengerjakan"));
            } else {
                hasil.add(new SiswaSkorDTO(siswa.getId(), siswa.getUsername(), null, "Belum mengerjakan"));
            }
        }

        return hasil;
    }
}
