package com.example.ZeQuiz.entity;

import jakarta.persistence.*;
import lombok.*;
import com.example.ZeQuiz.entity.Kelas;


import java.time.LocalDate;

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

    private int timer;

    @Column(name = "jumlah_soal")
    private int jumlahSoal;

    private LocalDate tanggal;

    @ManyToOne
    @JoinColumn(name = "topik_id", nullable = false)
    private Topik topik;

    @ManyToOne
    @JoinColumn(name = "guru_id", nullable = false)
    private User guru;

    @ManyToOne
    @JoinColumn(name = "kelas_id", nullable = false)
    private Kelas kelas;

}
