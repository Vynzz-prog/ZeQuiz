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


    public Skor hitungSkor(User user, Kuis kuis, List<JawabanSiswa> jawabanSiswaList) {

        if (!user.getKelas().getId().equals(kuis.getKelas().getId())) {
            throw new RuntimeException("Siswa tidak memiliki akses ke kuis ini karena berbeda kelas.");
        }

        Optional<Skor> existing = skorRepository.findBySiswaAndKuis(user, kuis);
        if (existing.isPresent()) {
            throw new RuntimeException("Kuis ini sudah pernah dikerjakan.");
        }

        List<KuisSoal> kuisSoalList = kuisSoalRepository.findByKuis(kuis);
        if (kuisSoalList.isEmpty()) {
            throw new RuntimeException("Soal dalam kuis tidak ditemukan.");
        }

        Set<Long> validSoalIds = kuisSoalList.stream()
                .map(ks -> ks.getSoal().getId())
                .collect(Collectors.toSet());

        double skorPerSoal = 100.0 / validSoalIds.size();
        double score = 0;

        for (JawabanSiswa jawaban : jawabanSiswaList) {
            if (!validSoalIds.contains(jawaban.getSoalId())) continue;

            Soal soal = soalRepository.findById(jawaban.getSoalId()).orElse(null);
            if (soal != null) {
                String jawabanBenar = soal.getJawabanBenar() != null ? soal.getJawabanBenar().trim().toLowerCase() : "";
                String jawabanSiswa = jawaban.getJawabanDipilih() != null ? jawaban.getJawabanDipilih().trim().toLowerCase() : "";

                if (jawabanBenar.equals(jawabanSiswa)) {
                    score += skorPerSoal;
                }
            }
        }

        Skor skor = new Skor();
        skor.setSiswa(user);
        skor.setKuis(kuis);
        skor.setSkor((int) Math.round(score));
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
