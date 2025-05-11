# API SPEC MURID

### Lihat Soal Saat Kuis

## Endpoint : GET zequiz/kuis/{kuisId}/soal

- contoh : zequiz/kuis/1/soal

- Menampilkan soal saat mengerjakan kuis


### response body (Success) :
```json
[
  {
    "id": 3,
    "pertanyaan": "Selesaikan Pertanyaan di bawah ini",
    "gambar": "images.jpg",
    "opsiA": "30",
    "opsiB": "7",
    "opsiC": "50",
    "opsiD": "56"
  },
  {
    "id": 1,
    "pertanyaan": "5 x 6 = ....?",
    "gambar": null,
    "opsiA": "30",
    "opsiB": "23",
    "opsiC": "50",
    "opsiD": "56"
  }
]
```

### response body (Faild) :
```json
{
  "pesan": "Akses ditolak: hanya siswa di kelas yang sama yang dapat mengerjakan kuis ini"
}
```


### Kirim Jawaban + Hitung Skor

## Endpoint : POST zequiz/skor/hitung
- Param :
    kuisId

- contoh : zequiz/skor/hitung?kuisId=4

- Mengirim jawaban saat kuis dan mendapat skor

- kalo dia so pernah kirim ( so pernah bekeng ni kuis ), dia nda bisa jawab ulang


### request body :
```json
[
  {
    "soalId": 10,
    "jawabanDipilih": "A"
  },
  {
    "soalId": 3,
    "jawabanDipilih": "C"
  }
]
```

### response body (Success) :
```json
{
  "id": 60,
  "kuis": {
    "id": 29,
    "nama": "Kuis Matematika Bab 2",
    "timer": 120,
    "jumlahSoal": 1,
    "tanggal": "2025-05-11",
    "topik": {
      "id": 11,
      "nama": "gambar"
    },
    "kelas": {
      "id": 1,
      "nama": "Kelas 4"
    }
  },
  "siswaId": 5,
  "skor": 100
}
```

### response body (Faild) :
```json
{
  "pesan": "401 Unauthorized"
}
```

### Lihat Skor untuk murid

## Endpoint : GET zequiz/skor/kuis/{kuisId}

- contoh : zequiz/skor/kuis/5


### response body (Success) :
```json
{
  "id": 60,
  "kuis": {
    "id": 29,
    "nama": "Kuis Matematika Bab 2",
    "timer": 120,
    "jumlahSoal": 1,
    "tanggal": "2025-05-11",
    "topik": {
      "id": 11,
      "nama": "gambar"
    },
    "kelas": {
      "id": 1,
      "nama": "Kelas 4"
    }
  },
  "siswaId": 5,
  "skor": 100
}
```

### response body (Faild) :
```json
{
  "pesan": "401 Unauthorized // Kuis tidak ditemukan // Skor tidak ditemukan"
}
```