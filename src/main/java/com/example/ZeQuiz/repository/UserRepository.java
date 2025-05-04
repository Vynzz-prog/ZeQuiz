package com.example.ZeQuiz.repository;

import com.example.ZeQuiz.entity.Kelas;
import com.example.ZeQuiz.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findByUsername(String username);


    boolean existsByUsername(String username);


    List<User> findByRoleAndKelas(String role, Kelas kelas);
}
