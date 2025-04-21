package com.example.ZeQuiz.model;

public class JawabanSiswa {
    private Long soalId;
    private String jawabanDipilih;
    private boolean correct;

    public Long getSoalId() {
        return soalId;
    }

    public void setSoalId(Long soalId) {
        this.soalId = soalId;
    }

    public String getJawabanDipilih() {
        return jawabanDipilih;
    }

    public void setJawabanDipilih(String jawabanDipilih) {
        this.jawabanDipilih = jawabanDipilih;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
}
