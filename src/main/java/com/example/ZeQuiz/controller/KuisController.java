package com.example.ZeQuiz.controller;

import com.example.ZeQuiz.entity.Kuis;
import com.example.ZeQuiz.service.KuisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("zequiz/kuis")
public class KuisController {

    @Autowired
    private KuisService kuisService;

    // Endpoint untuk membuat kuis baru
    @PostMapping("/buat")
    public ResponseEntity<Kuis> buatKuis(@RequestBody Kuis kuis, @RequestParam Long topikId) {
        Kuis newKuis = kuisService.buatKuis(kuis, topikId);
        return ResponseEntity.status(HttpStatus.CREATED).body(newKuis);
    }

    // Endpoint untuk mendapatkan kuis berdasarkan kelas
    @GetMapping("/{kelasId}")
    public ResponseEntity<List<Kuis>> getKuisByKelas(@PathVariable Long kelasId) {
        List<Kuis> kuisList = kuisService.getKuisByKelas(kelasId);  // Ganti method ini
        return ResponseEntity.ok().body(kuisList);
    }
}
