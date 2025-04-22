package com.example.ZeQuiz.controller;

import com.example.ZeQuiz.entity.Skor;
import com.example.ZeQuiz.entity.Kuis;
import com.example.ZeQuiz.entity.User;
import com.example.ZeQuiz.model.JawabanSiswa;
import com.example.ZeQuiz.service.SkorService;
import com.example.ZeQuiz.service.UserService;
import com.example.ZeQuiz.service.KuisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("zequiz/skor")
public class SkorController {

    @Autowired
    private SkorService skorService;

    @Autowired
    private UserService userService;

    @Autowired
    private KuisService kuisService;

    @PostMapping("/hitung")
    public ResponseEntity<Skor> hitungSkor(@RequestBody List<JawabanSiswa> jawabanSiswaList,
                                           @RequestParam Long userId,
                                           @RequestParam Long kuisId) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));

        Kuis kuis = kuisService.findById(kuisId)
                .orElseThrow(() -> new RuntimeException("Kuis tidak ditemukan"));

        Skor skor = skorService.hitungSkor(user, kuis, jawabanSiswaList);
        return ResponseEntity.ok(skor);
    }

    @GetMapping("/kuis/{kuisId}/siswa/{userId}")
    public ResponseEntity<Skor> getSkor(@PathVariable Long kuisId,
                                        @PathVariable Long userId) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));

        Kuis kuis = kuisService.findById(kuisId)
                .orElseThrow(() -> new RuntimeException("Kuis tidak ditemukan"));

        Skor skor = skorService.getSkorByUserAndKuis(user, kuis); // pakai 'siswa' di dalam service
        return ResponseEntity.ok(skor);
    }
}
