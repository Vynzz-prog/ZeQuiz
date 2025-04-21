package com.example.ZeQuiz.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "skor")
public class Skor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "kuis_id", nullable = false)
    private Kuis kuis;

    @ManyToOne
    @JoinColumn(name = "siswa_id", nullable = false)
    private User siswa;

    @Column(name = "skor")
    private Integer skor;

    @Column(name = "waktu_selesai")
    private LocalDateTime waktuSelesai;
}
