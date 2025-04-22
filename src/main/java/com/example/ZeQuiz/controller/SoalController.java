package com.example.ZeQuiz.controller;

import com.example.ZeQuiz.model.SoalRequest;
import com.example.ZeQuiz.service.SoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("zequiz/soal")
public class SoalController {

    @Autowired
    private SoalService soalService;

    @PostMapping
    public ResponseEntity<?> createSoalWithImage(
            @RequestPart("data") SoalRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        try {
            String imagePath = null;

            if (file != null && !file.isEmpty()) {
                if (file.getSize() > 1_000_000 ||
                        !(file.getContentType().contains("jpeg") || file.getContentType().contains("png"))) {
                    return ResponseEntity.badRequest().body("File must be .jpg or .png and under 1MB");
                }

                imagePath = soalService.saveImage(file);
            }

            soalService.createSoal(request, imagePath);
            return ResponseEntity.ok("Soal berhasil dibuat");

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
}
