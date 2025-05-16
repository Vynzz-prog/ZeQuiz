package com.example.ZeQuiz.controller;

import com.example.ZeQuiz.dto.KuisResponseDTO;
import com.example.ZeQuiz.dto.SoalDTO;
import com.example.ZeQuiz.entity.Kuis;
import com.example.ZeQuiz.entity.User;
import com.example.ZeQuiz.service.KuisService;
import com.example.ZeQuiz.service.UserService;
import com.example.ZeQuiz.repository.KuisSoalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/zequiz/kuis")
public class KuisController {

    @Autowired
    private KuisService kuisService;

    @Autowired
    private UserService userService;

    @Autowired
    private KuisSoalRepository kuisSoalRepository;

    @PostMapping("/buat")
    public ResponseEntity<?> buatKuis(@RequestParam Long topikId,
                                      @RequestBody Kuis kuisInput,
                                      @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User guru = userService.findByUsername(userDetails.getUsername());
            Kuis kuis = kuisService.buatKuis(guru.getId(), topikId, kuisInput);

            // Tangani kemungkinan null secara aman
            Map<String, Object> topikMap = new java.util.HashMap<>();
            if (kuis.getTopik() != null) {
                topikMap.put("id", kuis.getTopik().getId());
                topikMap.put("nama", kuis.getTopik().getNama());
            }

            Map<String, Object> kelasMap = new java.util.HashMap<>();
            if (kuis.getKelas() != null) {
                kelasMap.put("id", kuis.getKelas().getId());
                kelasMap.put("nama", kuis.getKelas().getNama());
            }

            Map<String, Object> response = new java.util.HashMap<>();
            response.put("id", kuis.getId());
            response.put("nama", kuis.getNama());
            response.put("timer", kuis.getTimer());
            response.put("jumlahSoal", kuis.getJumlahSoal());
            response.put("tanggal", kuis.getTanggal().format(DateTimeFormatter.ISO_DATE));
            response.put("waktuMulai", kuis.getWaktuMulai() != null
                    ? kuis.getWaktuMulai().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                    : null);
            response.put("waktuSelesai", kuis.getWaktuSelesai() != null
                    ? kuis.getWaktuSelesai().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                    : null);
            response.put("topik", topikMap);
            response.put("kelas", kelasMap);

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("pesan", e.getMessage()));
        }
    }


    @GetMapping("/kelas/{kelasId}")
    public ResponseEntity<?> getKuisByKelas(@PathVariable Long kelasId) {
        try {
            List<Kuis> kuisList = kuisService.getKuisByKelas(kelasId);
            List<KuisResponseDTO> responseList = kuisList.stream()
                    .map(k -> KuisResponseDTO.builder()
                            .id(k.getId())
                            .namaKuis(k.getNama())
                            .timer(k.getTimer())
                            .jumlahSoal(k.getJumlahSoal())
                            .tanggal(k.getTanggal().toString())
                            .namaTopik(k.getTopik().getNama())
                            .waktuMulai(k.getWaktuMulai() != null
                                    ? k.getWaktuMulai().format (DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"))
                                    : null)
                            .waktuSelesai(k.getWaktuSelesai() != null
                                    ? k.getWaktuSelesai().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"))
                                    : null)
                            .build())
                    .toList();
            return ResponseEntity.ok(responseList);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("pesan", e.getMessage()));
        }
    }

    @GetMapping("/{kuisId}/soal")
    public ResponseEntity<?> getSoalByKuis(@PathVariable Long kuisId,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        Kuis kuis = kuisService.findById(kuisId);

        if (!"SISWA".equalsIgnoreCase(user.getRole()) ||
                !kuis.getKelas().getId().equals(user.getKelas().getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("pesan", "Akses ditolak: hanya siswa di kelas yang sama yang dapat mengerjakan kuis ini"));
        }

        // ✅ Validasi waktu
        LocalDateTime now = LocalDateTime.now();
        if (kuis.getWaktuMulai() != null && kuis.getWaktuSelesai() != null) {
            if (now.isBefore(kuis.getWaktuMulai()) || now.isAfter(kuis.getWaktuSelesai())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("pesan", "Kuis hanya bisa dikerjakan pada waktu yang telah ditentukan"));
            }
        }

        List<SoalDTO> soal = kuisService.getSoalDariKuis(kuisId).stream()
                .map(s -> new SoalDTO(
                        s.getId(),
                        s.getPertanyaan(),
                        s.getGambar(),
                        s.getOpsiA(),
                        s.getOpsiB(),
                        s.getOpsiC(),
                        s.getOpsiD()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(soal);
    }

    @GetMapping("/{kuisId}")
    public ResponseEntity<?> getKuisById(@PathVariable Long kuisId) {
        try {
            Kuis kuis = kuisService.findById(kuisId); // pastikan method ini sudah ada di service
            Map<String, Object> response = new java.util.HashMap<>();
            response.put("id", kuis.getId());
            response.put("nama", kuis.getNama());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("pesan", e.getMessage()));
        }
    }


}
