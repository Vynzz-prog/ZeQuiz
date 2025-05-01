# API SPEC GURU

### Buat Topik

## Endpoint : POST zequiz/topik/buat

- Menyimpan topik baru ke database

### request body :
```json
{
  "namaTopik": "Aljabar"
}
```

### response body (Success) :
```json
{
  "id": 1,
  "nama": "Aljabar",
  "kelas": {
    "id": 1,
    "nama": "Kelas 4"
  }
}
```

### response body (Faild) :
```json
{
  "pesan" : "error"
}
```

### Tampil Topik

## Endpoint : GET zequiz/topik/my

- menampilkan daftar topik oleh guru berdasarkan kelas 
 
### response body (Success) :
```json
[
    {
        "id": 1,
        "nama": "Perkalian",
        "kelas": {
            "id": 1,
            "nama": "Kelas 4"
        }
    },
    {
        "id": 3,
        "nama": "Penjumlahan",
        "kelas": {
            "id": 1,
            "nama": "Kelas 4"
        }
    },
    {
        "id": 4,
        "nama": "Aljabar",
        "kelas": {
            "id": 1,
            "nama": "Kelas 4"
        }
    }
]
```

### response body (Faild) :
```json
{
  "pesan" : "error"
}
```

# Hapus Topik
## Endpoint : DELETE zequiz/topik/hapus/{topikId}

- contoh : zequiz/topik/hapus/7

- Hapus topik yang ada di suatu kelas

### response body (Success) :
```json
{
  "message": "Topik berhasil dihapus"
}
```

### response body (Faild) :
```json
{
  "pesan" : "error contoh : Hanya guru yang dapat menghapus topik // Guru kelas 4 tidak memiliki akses di topik kelas 5"
}
```


### Buat Kuis

## Endpoint : POST zequiz/kuis/buat?userId=1&topikId=1

- Param
userId = value
topikId = value

### request body :
```json
{

  "timer": 120,
  "jumlahSoal": 2

}
```

### response body (Success) :
```json
{
  "topik": {
    "nama": "Perkalian",
    "id": 1
  },
  "kelas": {
    "nama": "Kelas 4",
    "id": 1
  },
  "jumlahSoal": 2,
  "timer": 120,
  "tanggal": "2025-04-29",
  "id": 4
}
```

### response body (Faild) :
```json
{
  "pesan" : "error contoh : Hanya guru yang dapat membuat kuis / soal di bank soal nda cukup"
}
```


## Buat Soal
### Endpoint : POST zequiz/soal/tambah

- buat soal per topik

- Content-Type: multipart/form-data 

### Request :
topikId: 1 
pertanyaan: Berapa hasil 2 x 3?
opsiA: 5
opsiB: 6
opsiC: 7
opsiD: 8
jawabanBenar: B
gambarFile: (optional)

### response body (Success) :
```json
{
  "id": 9,
  "topik": {
    "id": 1,
    "nama": "Perkalian",
    "kelas": {
      "id": 1,
      "nama": "Kelas 4"
    }
  },
  "pertanyaan": "Berapa 10 x 5 ?",
  "gambar": null,
  "opsiA": "30",
  "opsiB": "7",
  "opsiC": "50",
  "opsiD": "56",
  "jawabanBenar": "C"
}
```

### response body (Faild) :
```json
{
  "pesan" : "unauthorized"
}
```

# Hapus Soal
## Endpoint : DELETE zequiz/soal/hapus/{soalId}

- Hapus soal berdasarkan topik

- contoh : zequiz/soal/hapus/6

### response body (Success) :
```json
{
  "pesan" : "Soal berhasil dihapus"
}
```

### response body (Faild) :
```json
{
  "pesan" : "unauthorized 401"
}
```

# Lihat Soal
## Endpoint : GET zequiz/soal/topik/{topikId}

- contoh : zequiz/soal/topik/1

- Lihat soal berdasarkan topik yang sesuai kelas


### response body (Success) :
```json
[
  {
    "id": 1,
    "topik": {
      "id": 1,
      "nama": "Perkalian",
      "kelas": {
        "id": 1,
        "nama": "Kelas 4"
      }
    },
    "pertanyaan": "5 x 6 = ....?",
    "gambar": null,
    "opsiA": "30",
    "opsiB": "23",
    "opsiC": "50",
    "opsiD": "56",
    "jawabanBenar": "A"
  },
  {
    "id": 3,
    "topik": {
      "id": 1,
      "nama": "Perkalian",
      "kelas": {
        "id": 1,
        "nama": "Kelas 4"
      }
    },
    "pertanyaan": "Selesaikan Pertanyaan di bawah ini",
    "gambar": "images.jpg",
    "opsiA": "30",
    "opsiB": "7",
    "opsiC": "50",
    "opsiD": "56",
    "jawabanBenar": "B"
  }
]
```

### response body (Faild) :
```json
{
  "pesan" : "unauthorized 401"
}
```

### Lihat Skor untuk guru

## Endpoint : GET zequiz/skor/guru/kuis/{kuisId}

- contoh : zequiz/skor/guru/kuis/4


### response body (Success) :
```json
[
  {
    "siswaId": 5,
    "username": "ucup",
    "skor": null,
    "status": "Belum mengerjakan"
  },
  {
    "siswaId": 7,
    "username": "Vincent",
    "skor": 50,
    "status": "Sudah mengerjakan"
  }
]
```

### response body (Faild) :
```json
{

  "error": "Akses ditolak ke kuis ini" 

}
```
- akses di tolak karena guru mo coba akses kuis dari kelas lain punya

