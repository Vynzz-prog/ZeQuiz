package com.example.ZeQuiz.controller;

import com.example.ZeQuiz.dto.*;
import com.example.ZeQuiz.entity.Kuis;
import com.example.ZeQuiz.entity.Skor;
import com.example.ZeQuiz.entity.User;
import com.example.ZeQuiz.model.JawabanSiswa;
import com.example.ZeQuiz.service.KuisService;
import com.example.ZeQuiz.service.SkorService;
import com.example.ZeQuiz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/zequiz/skor")
public class SkorController {

    @Autowired private SkorService skorService;
    @Autowired private UserService userService;
    @Autowired private KuisService kuisService;

    /**
     * ✅ Endpoint untuk siswa menghitung dan menyimpan skor kuis.
     * ❌ Guru tidak bisa mengakses endpoint ini.
     */
    @PostMapping("/hitung")
    public ResponseEntity<?> hitungSkor(
            @RequestBody List<JawabanSiswa> jawabanSiswaList,
            @RequestParam Long kuisId,
            @AuthenticationPrincipal UserDetails ud
    ) {
        User user = userService.findByUsername(ud.getUsername());

        if (!"SISWA".equalsIgnoreCase(user.getRole())) {
            return ResponseEntity.status(403).body(Map.of("pesan", "Hanya siswa yang dapat mengerjakan kuis."));
        }

        Kuis kuis = kuisService.findById(kuisId);
        Skor s = skorService.hitungSkor(user, kuis, jawabanSiswaList);
        return ResponseEntity.ok(mapToDto(s));
    }

    /**
     * ✅ Endpoint untuk siswa melihat skor mereka sendiri.
     */
    @GetMapping("/kuis/{kuisId}")
    public ResponseEntity<?> getSkor(
            @PathVariable Long kuisId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        try {
            User user = userService.findByUsername(userDetails.getUsername());
            Kuis kuis = kuisService.findById(kuisId);

            Skor skor = skorService.getSkorByUserAndKuis(user, kuis);
            return ResponseEntity.ok(mapToDto(skor));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("pesan", e.getMessage()));
        }
    }

    /**
     * ✅ Endpoint untuk guru melihat semua skor siswa dalam satu kuis.
     */
    @GetMapping("/guru/kuis/{kuisId}")
    public ResponseEntity<?> getSkorSiswaByKuis(
            @PathVariable Long kuisId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        try {
            User guru = userService.findByUsername(userDetails.getUsername());
            Kuis kuis = kuisService.findById(kuisId);

            if (!"GURU".equalsIgnoreCase(guru.getRole()) || !kuis.getKelas().getId().equals(guru.getKelas().getId())) {
                return ResponseEntity.status(403).body(Map.of("pesan", "Akses ditolak ke kuis ini"));
            }

            List<User> siswaList = userService.getSiswaByKelas(guru.getKelas());
            return ResponseEntity.ok(skorService.getStatusPengerjaanKuis(kuis, siswaList));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("pesan", e.getMessage()));
        }
    }

    /**
     * 🔁 Konversi entity Skor ke SkorResponseDTO (termasuk nama kuis).
     */
    private SkorResponseDTO mapToDto(Skor s) {
        return SkorResponseDTO.builder()
                .id(s.getId())
                .kuis(KuisSimpleDTO.builder()
                        .id(s.getKuis().getId())
                        .nama(s.getKuis().getNama()) // 🆕 Tambahan nama kuis
                        .timer(s.getKuis().getTimer())
                        .jumlahSoal(s.getKuis().getJumlahSoal())
                        .tanggal(s.getKuis().getTanggal().toString())
                        .topik(TopikSimpleDTO.builder()
                                .id(s.getKuis().getTopik().getId())
                                .nama(s.getKuis().getTopik().getNama())
                                .build())
                        .kelas(KelasSimpleDTO.builder()
                                .id(s.getKuis().getKelas().getId())
                                .nama(s.getKuis().getKelas().getNama())
                                .build())
                        .build())
                .siswaId(s.getSiswa().getId())
                .skor(s.getSkor())
                .build();
    }
}
