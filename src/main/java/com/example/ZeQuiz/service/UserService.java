package com.example.ZeQuiz.service;

import com.example.ZeQuiz.entity.User;
import com.example.ZeQuiz.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan dengan username: " + username));
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

}
