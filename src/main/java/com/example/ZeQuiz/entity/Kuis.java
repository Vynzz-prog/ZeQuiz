package com.example.ZeQuiz.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "kuis")
public class Kuis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nama;

    private int timer;

    @Column(name = "jumlah_soal")
    private int jumlahSoal;

    private LocalDate tanggal;

    @Column(name = "waktu_mulai")
    private LocalDateTime waktuMulai;

    @Column(name = "waktu_selesai")
    private LocalDateTime waktuSelesai;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "topik_id", nullable = false)
    private Topik topik;

    @ManyToOne
    @JoinColumn(name = "guru_id", nullable = false)
    private User guru;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "kelas_id", nullable = false)
    private Kelas kelas;
}
