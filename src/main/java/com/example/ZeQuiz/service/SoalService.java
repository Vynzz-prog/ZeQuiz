package com.example.ZeQuiz.service;

import com.example.ZeQuiz.entity.Soal;
import com.example.ZeQuiz.entity.Topik;
import com.example.ZeQuiz.model.SoalRequest;
import com.example.ZeQuiz.repository.SoalRepository;
import com.example.ZeQuiz.repository.TopikRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Service
public class SoalService {

    @Autowired
    private SoalRepository soalRepository;

    @Autowired
    private TopikRepository topikRepository;

    public void createSoal(SoalRequest req, String imagePath) {
        Optional<Topik> optionalTopik = topikRepository.findById(req.getTopikId());
        if (optionalTopik.isEmpty()) {
            throw new RuntimeException("Topik tidak ditemukan");
        }

        Soal soal = Soal.builder()
                .topik(optionalTopik.get())
                .pertanyaan(req.getPertanyaan())
                .opsiA(req.getOpsiA())
                .opsiB(req.getOpsiB())
                .opsiC(req.getOpsiC())
                .opsiD(req.getOpsiD())
                .jawabanBenar(req.getJawabanBenar())
                .gambar(imagePath)
                .build();

        soalRepository.save(soal);
    }

    public String saveImage(MultipartFile file) throws IOException {
        String folder = "uploads/";
        File dir = new File(folder);
        if (!dir.exists()) dir.mkdirs();

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        String filePath = folder + fileName;
        file.transferTo(new File(filePath));

        return "/images/" + fileName; // untuk diakses dari URL
    }
}
