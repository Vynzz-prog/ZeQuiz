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

    // Buat topik (kelas diambil otomatis dari akun guru yang sedang login)
    @PostMapping("/buat")
    public ResponseEntity<Topik> buatTopik(@RequestBody Map<String, String> body,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        String namaTopik = body.get("namaTopik");
        if (namaTopik == null || namaTopik.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        User user = userService.findByUsername(userDetails.getUsername());
        Topik topik = topikService.buatTopik(namaTopik, user.getId());
        return ResponseEntity.ok(topik);
    }

    // Ambil semua topik untuk user yang sedang login
    @GetMapping("/my")
    public ResponseEntity<List<Topik>> getTopikByUser(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        List<Topik> topikList = topikService.getTopikByUser(user.getId());
        return ResponseEntity.ok(topikList);
    }
}
