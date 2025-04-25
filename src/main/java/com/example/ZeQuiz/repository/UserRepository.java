package com.example.ZeQuiz.repository;

import com.example.ZeQuiz.entity.Kelas;
import com.example.ZeQuiz.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Mencari user berdasarkan username.
     */
    Optional<User> findByUsername(String username);

    /**
     * Mengecek apakah username sudah digunakan.
     */
    boolean existsByUsername(String username);

    /**
     * Mengambil semua user dengan role tertentu dan kelas tertentu (khusus siswa).
     */
    List<User> findByRoleAndKelas(String role, Kelas kelas);
}
