package com.example.ZeQuiz.controller;

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

    @Autowired
    private SkorService skorService;

    @Autowired
    private UserService userService;

    @Autowired
    private KuisService kuisService;

    /**
     * Endpoint untuk siswa menghitung dan menyimpan skor kuis.
     */
    @PostMapping("/hitung")
    public ResponseEntity<?> hitungSkor(@RequestBody List<JawabanSiswa> jawabanSiswaList,
                                        @RequestParam Long kuisId,
                                        @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = userService.findByUsername(userDetails.getUsername());
            Kuis kuis = kuisService.findById(kuisId);

            Skor skor = skorService.hitungSkor(user, kuis, jawabanSiswaList);
            return ResponseEntity.ok(skor);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Endpoint untuk siswa mengambil skornya sendiri untuk suatu kuis.
     */
    @GetMapping("/kuis/{kuisId}")
    public ResponseEntity<?> getSkor(@PathVariable Long kuisId,
                                     @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = userService.findByUsername(userDetails.getUsername());
            Kuis kuis = kuisService.findById(kuisId);

            Skor skor = skorService.getSkorByUserAndKuis(user, kuis);
            return ResponseEntity.ok(skor);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Endpoint untuk guru melihat semua skor siswa pada suatu kuis.
     */
    @GetMapping("/guru/kuis/{kuisId}")
    public ResponseEntity<?> getSkorSiswaByKuis(@PathVariable Long kuisId,
                                                @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User guru = userService.findByUsername(userDetails.getUsername());
            Kuis kuis = kuisService.findById(kuisId);

            // Validasi guru hanya bisa akses kuis di kelasnya
            if (!kuis.getKelas().getId().equals(guru.getKelas().getId())) {
                return ResponseEntity.status(403).body(Map.of("error", "Akses ditolak ke kuis ini"));
            }

            // Ambil semua siswa dari kelas guru
            List<User> siswaList = userService.getSiswaByKelas(guru.getKelas());

            return ResponseEntity.ok(skorService.getStatusPengerjaanKuis(kuis, siswaList));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
