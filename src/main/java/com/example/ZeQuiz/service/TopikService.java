package com.example.ZeQuiz.service;

import com.example.ZeQuiz.entity.Kelas;
import com.example.ZeQuiz.entity.Topik;
import com.example.ZeQuiz.entity.User;
import com.example.ZeQuiz.repository.TopikRepository;
import com.example.ZeQuiz.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopikService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TopikRepository topikRepository;

    public Topik buatTopik(String namaTopik, Long userId) {
        User guru = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));

        Kelas kelasGuru = guru.getKelas();
        if (kelasGuru == null) {
            throw new RuntimeException("Guru tidak memiliki kelas");
        }

        Topik topik = new Topik();
        topik.setNama(namaTopik);
        topik.setKelas(kelasGuru);

        return topikRepository.save(topik);
    }

    public List<Topik> getTopikByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));
        return topikRepository.findByKelas(user.getKelas());
    }

    public void hapusTopik(Long topikId, User guru) {
        Topik topik = topikRepository.findById(topikId)
                .orElseThrow(() -> new RuntimeException("Topik tidak ditemukan"));

        // Validasi guru hanya bisa hapus topik milik kelasnya
        if (!topik.getKelas().getId().equals(guru.getKelas().getId())) {
            throw new RuntimeException("Anda tidak memiliki izin untuk menghapus topik ini");
        }

        topikRepository.delete(topik);
    }

}
