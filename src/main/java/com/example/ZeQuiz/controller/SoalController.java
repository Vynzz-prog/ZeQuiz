package com.example.ZeQuiz.controller;

import com.example.ZeQuiz.entity.Soal;
import com.example.ZeQuiz.entity.User;
import com.example.ZeQuiz.service.SoalService;
import com.example.ZeQuiz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("zequiz/soal")
public class SoalController {

    @Autowired
    private SoalService soalService;

    @Autowired
    private UserService userService;

    // ✅ Tambah soal (dengan upload gambar opsional)
    @PostMapping(value = "/tambah", consumes = "multipart/form-data")
    public ResponseEntity<?> tambahSoal(
            @RequestParam Long topikId,
            @RequestParam(required = false) String pertanyaan,
            @RequestParam String opsiA,
            @RequestParam String opsiB,
            @RequestParam String opsiC,
            @RequestParam String opsiD,
            @RequestParam String jawabanBenar,
            @RequestParam(required = false) MultipartFile gambarFile,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        try {
            User guru = userService.findByUsername(userDetails.getUsername());

            if (!"GURU".equalsIgnoreCase(guru.getRole())) {
                return ResponseEntity.status(403).body(Map.of("pesan", "Hanya guru yang dapat menambah soal"));
            }

            Soal soal = new Soal();
            soal.setPertanyaan(pertanyaan);
            soal.setOpsiA(opsiA);
            soal.setOpsiB(opsiB);
            soal.setOpsiC(opsiC);
            soal.setOpsiD(opsiD);
            soal.setJawabanBenar(jawabanBenar);

            if (gambarFile != null && !gambarFile.isEmpty()) {
                soal.setGambar(gambarFile.getOriginalFilename());
                // ❗ Tambahkan simpan file jika perlu
            }

            Soal created = soalService.tambahSoal(guru, topikId, soal);
            return ResponseEntity.ok(created);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("pesan", e.getMessage()));
        }
    }

    // ✅ Hapus soal
    @DeleteMapping("/hapus/{soalId}")
    public ResponseEntity<?> hapusSoal(@PathVariable Long soalId,
                                       @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User guru = userService.findByUsername(userDetails.getUsername());

            if (!"GURU".equalsIgnoreCase(guru.getRole())) {
                return ResponseEntity.status(403).body(Map.of("pesan", "Hanya guru yang dapat menghapus soal"));
            }

            soalService.hapusSoal(soalId, guru);
            return ResponseEntity.ok(Map.of("pesan", "Soal berhasil dihapus"));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("pesan", e.getMessage()));
        }
    }

    // ✅ Lihat semua soal dalam satu topik (hanya guru)
    @GetMapping("/topik/{topikId}")
    public ResponseEntity<?> getSoalByTopik(@PathVariable Long topikId,
                                            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User guru = userService.findByUsername(userDetails.getUsername());

            if (!"GURU".equalsIgnoreCase(guru.getRole())) {
                return ResponseEntity.status(403).body(Map.of("pesan", "Hanya guru yang dapat melihat soal"));
            }

            List<Soal> soalList = soalService.getSoalByTopik(topikId, guru);
            return ResponseEntity.ok(soalList);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("pesan", e.getMessage()));
        }
    }
}
