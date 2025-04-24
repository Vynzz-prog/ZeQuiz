package com.example.ZeQuiz.service;

import com.example.ZeQuiz.entity.Soal;
import com.example.ZeQuiz.entity.Topik;
import com.example.ZeQuiz.entity.User;
import com.example.ZeQuiz.repository.SoalRepository;
import com.example.ZeQuiz.repository.TopikRepository;
import com.example.ZeQuiz.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SoalService {

    @Autowired
    private SoalRepository soalRepository;

    @Autowired
    private TopikRepository topikRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Menambahkan soal ke dalam topik, hanya jika guru berasal dari kelas yang sesuai.
     */
    public Soal tambahSoal(Long userId, Long topikId, Soal inputSoal) {
        User guru = getGuruById(userId);
        Topik topik = getTopikById(topikId);

        // Validasi kelas topik harus sama dengan kelas guru
        if (!topik.getKelas().getId().equals(guru.getKelas().getId())) {
            throw new RuntimeException("❌ Topik bukan milik kelas guru ini");
        }

        inputSoal.setTopik(topik);
        return soalRepository.save(inputSoal);
    }

    /**
     * Menghapus soal jika guru berasal dari kelas yang sesuai.
     */
    public void hapusSoal(Long soalId, Long userId) {
        Soal soal = soalRepository.findById(soalId)
                .orElseThrow(() -> new RuntimeException("❌ Soal tidak ditemukan"));

        User guru = getGuruById(userId);

        if (!soal.getTopik().getKelas().getId().equals(guru.getKelas().getId())) {
            throw new RuntimeException("❌ Guru tidak memiliki akses untuk menghapus soal ini");
        }

        soalRepository.delete(soal);
    }

    /**
     * Mengambil semua soal dalam topik jika guru berasal dari kelas yang sesuai.
     */
    public List<Soal> getSoalByTopik(Long topikId, Long userId) {
        User guru = getGuruById(userId);
        Topik topik = getTopikById(topikId);

        if (!topik.getKelas().getId().equals(guru.getKelas().getId())) {
            throw new RuntimeException("❌ Guru tidak memiliki akses ke topik ini");
        }

        return soalRepository.findByTopik(topik);
    }

    // 🔧 Helper methods untuk ambil guru dan topik
    private User getGuruById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("❌ Guru tidak ditemukan"));
    }

    private Topik getTopikById(Long topikId) {
        return topikRepository.findById(topikId)
                .orElseThrow(() -> new RuntimeException("❌ Topik tidak ditemukan"));
    }
}
