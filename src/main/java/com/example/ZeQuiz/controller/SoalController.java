package com.example.ZeQuiz.controller;

import com.example.ZeQuiz.entity.Soal;
import com.example.ZeQuiz.entity.User;
import com.example.ZeQuiz.service.SoalService;
import com.example.ZeQuiz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("zequiz/soal")
public class SoalController {

    @Autowired
    private SoalService soalService;

    @Autowired
    private UserService userService;

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
        System.out.println("\uD83D\uDCC5 [DEBUG] Endpoint /tambah dipanggil");

        try {
            User guru = userService.findByUsername(userDetails.getUsername());
            System.out.println("✅ [DEBUG] Guru login: " + guru.getUsername());

            if (!"GURU".equalsIgnoreCase(guru.getRole())) {
                System.out.println("❌ [DEBUG] Role bukan guru");
                return ResponseEntity.status(403).body(Map.of("pesan", "Hanya guru yang dapat menambah soal"));
            }

            Soal soal = new Soal();
            soal.setPertanyaan(pertanyaan);
            soal.setOpsiA(opsiA);
            soal.setOpsiB(opsiB);
            soal.setOpsiC(opsiC);
            soal.setOpsiD(opsiD);
            soal.setJawabanBenar(jawabanBenar);
            System.out.println("✅ [DEBUG] Pertanyaan dan opsi sudah disiapkan");

            if (gambarFile != null && !gambarFile.isEmpty()) {
                System.out.println("\uD83D\uDCF7 [DEBUG] Menerima file gambar: " + gambarFile.getOriginalFilename());

                String uploadDir = System.getProperty("user.dir") + File.separator + "uploads" + File.separator;
                File dir = new File(uploadDir);
                if (!dir.exists()) {
                    boolean created = dir.mkdirs();
                    System.out.println("\uD83D\uDCC1 [DEBUG] Folder 'uploads' dibuat: " + created);
                }

                String fileName = System.currentTimeMillis() + "_" + gambarFile.getOriginalFilename();
                File destFile = new File(uploadDir + fileName);

                gambarFile.transferTo(destFile);
                System.out.println("✅ [DEBUG] Gambar berhasil disimpan di: " + destFile.getAbsolutePath());

                soal.setGambar(fileName);
            } else {
                System.out.println("ℹ️ [DEBUG] Tidak ada gambar yang dikirim");
            }

            Soal created = soalService.tambahSoal(guru, topikId, soal);
            System.out.println("✅ [DEBUG] Soal berhasil ditambahkan ke database: ID " + created.getId());
            return ResponseEntity.ok(created);

        } catch (RuntimeException e) {
            System.out.println("⚠️ [ERROR] RuntimeException: " + e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("pesan", e.getMessage()));
        } catch (Exception e) {
            System.out.println("❌ [ERROR] Exception umum saat simpan gambar: " + e.getMessage());
            return ResponseEntity.internalServerError().body(Map.of("pesan", "Gagal menyimpan gambar."));
        }
    }

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

    @GetMapping("/uploads/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        try {
            Path file = Paths.get(System.getProperty("user.dir"), "uploads", filename);
            Resource resource = new UrlResource(file.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/page")
    public ResponseEntity<?> getSoalByTopikPaginated(
            @RequestParam Long topikId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        try {
            User guru = userService.findByUsername(userDetails.getUsername());

            if (!"GURU".equalsIgnoreCase(guru.getRole())) {
                return ResponseEntity.status(403).body(Map.of("pesan", "Hanya guru yang dapat melihat soal"));
            }

            Page<Soal> soalPage = soalService.getSoalByTopikWithPagination(topikId, guru, page, size);
            return ResponseEntity.ok(soalPage);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("pesan", e.getMessage()));
        }
    }
}
