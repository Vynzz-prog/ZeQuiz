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
            return ResponseEntity.ok(Map.of(
                    "id", kuis.getId(),
                    "nama", kuis.getNama(),
                    "timer", kuis.getTimer(),
                    "jumlahSoal", kuis.getJumlahSoal(),
                    "tanggal", kuis.getTanggal().format(DateTimeFormatter.ISO_DATE),
                    "topik", Map.of(
                            "id", kuis.getTopik().getId(),
                            "nama", kuis.getTopik().getNama()
                    ),
                    "kelas", Map.of(
                            "id", kuis.getKelas().getId(),
                            "nama", kuis.getKelas().getNama()
                    )
            ));
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
}
