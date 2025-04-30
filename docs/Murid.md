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
  "error": "Akses ditolak: hanya siswa di kelas yang sama yang dapat mengerjakan kuis ini"
}
```