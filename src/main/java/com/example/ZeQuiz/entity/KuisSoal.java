package com.example.ZeQuiz.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "kuis_soal")
public class KuisSoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "kuis_id", nullable = false)
    private Kuis kuis;

    @ManyToOne
    @JoinColumn(name = "soal_id", nullable = false)
    private Soal soal;

    private Integer urutan; // nanti lia mo pake ato nda
}
