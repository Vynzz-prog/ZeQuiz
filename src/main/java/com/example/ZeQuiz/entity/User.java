package com.example.ZeQuiz.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "kata_sandi", nullable = false)
    private String kata_sandi;

    @Column(name = "kelas", nullable = false)
    private Integer kelas;

    public String getPassword() {
        return kata_sandi;
    }
}
