package com.example.ZeQuiz.controller;

import com.example.ZeQuiz.entity.Kuis;
import com.example.ZeQuiz.service.KuisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/zequiz/kuis")
public class KuisController {

    @Autowired
    private KuisService kuisService;

    /**
     * Endpoint untuk guru membuat kuis.
     * Sistem otomatis mengatur guru dan kelas berdasarkan userId,
     * dan menambahkan soal berdasarkan topik yang dipilih.
     */
    @PostMapping("/buat")
    public ResponseEntity<?> buatKuis(@RequestParam Long userId,
                                      @RequestParam Long topikId,
                                      @RequestBody Kuis kuisInput) {
        try {
            Kuis kuis = kuisService.buatKuis(userId, topikId, kuisInput);
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
