package com.example.ZeQuiz.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "soal")
public class Soal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "topik_id", nullable = false)
    private Topik topik;

    @Column(name = "pertanyaan", columnDefinition = "TEXT")
    private String pertanyaan; // Boleh kosong jika soal berupa gambar

    @Column(name = "gambar")
    private String gambar; // Nama/path file gambar (opsional)

    @Column(name = "opsi_a", nullable = false)
    private String opsiA;

    @Column(name = "opsi_b", nullable = false)
    private String opsiB;

    @Column(name = "opsi_c", nullable = false)
    private String opsiC;

    @Column(name = "opsi_d", nullable = false)
    private String opsiD;

    @Column(name = "jawaban_benar", nullable = false)
    private String jawabanBenar; // Harus "A", "B", "C", atau "D"
}
