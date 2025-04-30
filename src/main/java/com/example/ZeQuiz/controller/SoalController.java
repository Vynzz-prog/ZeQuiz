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

@RestController
@RequestMapping("zequiz/soal")
public class SoalController {

    @Autowired
    private SoalService soalService;

    @Autowired
    private UserService userService;

    // ✅ Tambah soal (dengan upload gambar opsional)
    @PostMapping(value = "/tambah", consumes = "multipart/form-data")
    public ResponseEntity<Soal> tambahSoal(
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
        User guru = userService.findByUsername(userDetails.getUsername());

        Soal soal = new Soal();
        soal.setPertanyaan(pertanyaan);
        soal.setOpsiA(opsiA);
        soal.setOpsiB(opsiB);
        soal.setOpsiC(opsiC);
        soal.setOpsiD(opsiD);
        soal.setJawabanBenar(jawabanBenar);

        if (gambarFile != null && !gambarFile.isEmpty()) {
            soal.setGambar(gambarFile.getOriginalFilename());
            // ❗ Kalau mau, bisa tambahkan simpan file gambar fisik di sini
        }

        Soal created = soalService.tambahSoal(guru, topikId, soal);  // ✅ kirim User, bukan ID
        return ResponseEntity.ok(created);
    }

    // ✅ Hapus soal
    @DeleteMapping("/hapus/{soalId}")
    public ResponseEntity<String> hapusSoal(@PathVariable Long soalId,
                                            @AuthenticationPrincipal UserDetails userDetails) {
        User guru = userService.findByUsername(userDetails.getUsername());
        soalService.hapusSoal(soalId, guru);  // ✅ kirim User, bukan ID
        return ResponseEntity.ok("Soal berhasil dihapus");
    }

    // ✅ Lihat semua soal dalam satu topik
    @GetMapping("/topik/{topikId}")
    public ResponseEntity<List<Soal>> getSoalByTopik(@PathVariable Long topikId,
                                                     @AuthenticationPrincipal UserDetails userDetails) {
        User guru = userService.findByUsername(userDetails.getUsername());
        List<Soal> soalList = soalService.getSoalByTopik(topikId, guru);  // ✅ kirim User, bukan ID
        return ResponseEntity.ok(soalList);
    }
}
