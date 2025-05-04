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


    @PostMapping("/buat")
    public ResponseEntity<?> buatTopik(@RequestBody Map<String, String> body,
                                       @AuthenticationPrincipal UserDetails userDetails) {
        String namaTopik = body.get("namaTopik");


        if (namaTopik == null || namaTopik.isBlank()) {
            System.out.println("GAGAL: namaTopik kosong!");
            return ResponseEntity.badRequest().body(Map.of("pesan", "Nama topik tidak boleh kosong"));
        }

        User user = userService.findByUsername(userDetails.getUsername());
        System.out.println("Role user: " + user.getRole());

        if (!"GURU".equalsIgnoreCase(user.getRole())) {
            System.out.println("GAGAL: bukan GURU");
            return ResponseEntity.status(403).body(Map.of("pesan", "Hanya guru yang dapat membuat topik"));
        }

        Topik topik = topikService.buatTopik(namaTopik, user.getId());
        System.out.println("BERHASIL: Topik disimpan dengan nama: " + topik.getNama());

        return ResponseEntity.ok(topik);
    }



    @GetMapping("/my")
    public ResponseEntity<?> getTopikByUser(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());

        if (!"GURU".equalsIgnoreCase(user.getRole())) {
            return ResponseEntity.status(403).body(Map.of("pesan", "Hanya guru yang dapat mengakses topik"));
        }

        List<Topik> topikList = topikService.getTopikByUser(user.getId());
        return ResponseEntity.ok(topikList);
    }


    @DeleteMapping("/hapus/{topikId}")
    public ResponseEntity<?> hapusTopik(@PathVariable Long topikId,
                                        @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = userService.findByUsername(userDetails.getUsername());

            if (!"GURU".equalsIgnoreCase(user.getRole())) {
                return ResponseEntity.status(403).body(Map.of("pesan", "Hanya guru yang dapat menghapus topik"));
            }

            topikService.hapusTopik(topikId, user);
            return ResponseEntity.ok(Map.of("pesan", "Topik berhasil dihapus"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("pesan", e.getMessage()));
        }
    }
}
