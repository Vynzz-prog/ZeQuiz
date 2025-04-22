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

@RestController
@RequestMapping("/zequiz/skor")
public class SkorController {

    @Autowired
    private SkorService skorService;

    @Autowired
    private UserService userService;

    @Autowired
    private KuisService kuisService;

    // Hitung skor setelah siswa menyelesaikan kuis
    @PostMapping("/hitung")
    public ResponseEntity<Skor> hitungSkor(@RequestBody List<JawabanSiswa> jawabanSiswaList,
                                           @RequestParam Long kuisId,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        Kuis kuis = kuisService.findById(kuisId); // ✅ perbaikan di sini

        Skor skor = skorService.hitungSkor(user, kuis, jawabanSiswaList);
        return ResponseEntity.ok(skor);
    }

    // Ambil skor siswa untuk suatu kuis
    @GetMapping("/kuis/{kuisId}")
    public ResponseEntity<Skor> getSkor(@PathVariable Long kuisId,
                                        @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        Kuis kuis = kuisService.findById(kuisId); // ✅ perbaikan di sini

        Skor skor = skorService.getSkorByUserAndKuis(user, kuis);
        return ResponseEntity.ok(skor);
    }
}
