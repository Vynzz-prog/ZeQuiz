package com.example.ZeQuiz.controller;

import com.example.ZeQuiz.entity.Topik;
import com.example.ZeQuiz.entity.User;
import com.example.ZeQuiz.service.TopikService;
import com.example.ZeQuiz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("zequiz/topik")
public class TopikController {

    @Autowired
    private TopikService topikService;

    @Autowired
    private UserService userService;

    // ✅ Buat topik (kelas diambil otomatis dari akun guru yang sedang login)
    @PostMapping("/buat")
    public ResponseEntity<?> buatTopik(@RequestBody Map<String, String> body,
                                       @AuthenticationPrincipal UserDetails userDetails) {
        String namaTopik = body.get("namaTopik");
        if (namaTopik == null || namaTopik.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Nama topik tidak boleh kosong"));
        }

        User user = userService.findByUsername(userDetails.getUsername());

        // ✅ Cek hanya guru yang bisa buat topik
        if (!"GURU".equalsIgnoreCase(user.getRole())) {
            return ResponseEntity.status(403).body(Map.of("error", "Hanya guru yang dapat membuat topik"));
        }

        Topik topik = topikService.buatTopik(namaTopik, user.getId());
        return ResponseEntity.ok(topik);
    }

    // ✅ Ambil semua topik untuk user yang sedang login (hanya guru)
    @GetMapping("/my")
    public ResponseEntity<?> getTopikByUser(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());

        if (!"GURU".equalsIgnoreCase(user.getRole())) {
            return ResponseEntity.status(403).body(Map.of("error", "Hanya guru yang dapat mengakses topik"));
        }

        List<Topik> topikList = topikService.getTopikByUser(user.getId());
        return ResponseEntity.ok(topikList);
    }

    // ✅ Hapus topik (hanya bisa dilakukan oleh guru)
    @DeleteMapping("/hapus/{topikId}")
    public ResponseEntity<?> hapusTopik(@PathVariable Long topikId,
                                        @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = userService.findByUsername(userDetails.getUsername());

            if (!"GURU".equalsIgnoreCase(user.getRole())) {
                return ResponseEntity.status(403).body(Map.of("error", "Hanya guru yang dapat menghapus topik"));
            }

            topikService.hapusTopik(topikId, user);
            return ResponseEntity.ok(Map.of("message", "Topik berhasil dihapus"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
