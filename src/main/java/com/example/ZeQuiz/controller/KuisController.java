package com.example.ZeQuiz.controller;

import com.example.ZeQuiz.entity.Kuis;
import com.example.ZeQuiz.entity.User;
import com.example.ZeQuiz.service.KuisService;
import com.example.ZeQuiz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/zequiz/kuis")
public class KuisController {

    @Autowired
    private KuisService kuisService;

    @Autowired
    private UserService userService;

    /**
     * Endpoint untuk guru membuat kuis.
     * Sistem otomatis mengatur guru dan kelas berdasarkan user yang sedang login,
     * dan menambahkan soal berdasarkan topik yang dipilih.
     */
    @PostMapping("/buat")
    public ResponseEntity<?> buatKuis(@RequestParam Long topikId,
                                      @RequestBody Kuis kuisInput,
                                      @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User guru = userService.findByUsername(userDetails.getUsername());
            Kuis kuis = kuisService.buatKuis(guru.getId(), topikId, kuisInput);
            return ResponseEntity.ok(kuis);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Endpoint untuk mengambil semua kuis berdasarkan kelas tertentu.
     */
    @GetMapping("/kelas/{kelasId}")
    public ResponseEntity<?> getKuisByKelas(@PathVariable Long kelasId) {
        try {
            List<Kuis> kuisList = kuisService.getKuisByKelas(kelasId);
            return ResponseEntity.ok(kuisList);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
