package com.example.ZeQuiz.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "topik")
public class Topik {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nama;

    @ManyToOne
    @JoinColumn(name = "kelas_id", nullable = false)
    private Kelas kelas;
}
