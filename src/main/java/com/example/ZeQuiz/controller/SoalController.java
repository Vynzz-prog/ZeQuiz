package com.example.ZeQuiz.controller;

import com.example.ZeQuiz.entity.Soal;
import com.example.ZeQuiz.service.SoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("zequiz/soal")
public class SoalController {

    @Autowired
    private SoalService soalService;

    @PostMapping("/tambah")
    public ResponseEntity<Soal> tambahSoal(@RequestParam Long userId,
                                           @RequestParam Long topikId,
                                           @RequestBody Soal soal) {
        Soal created = soalService.tambahSoal(userId, topikId, soal);
        return ResponseEntity.ok(created);
    }
}
