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
  "pesan": "Topik berhasil dihapus"
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
  "jumlahSoal": 1,
  "waktuMulai": "2025-05-11T02:00:00",
  "waktuSelesai": "2025-05-11T04:00:00"
}
```

### response body (Success) :
```json
{
  "timer": 60,
  "topik": {
    "id": 11,
    "nama": "gambar"
  },
  "id": 44,
  "waktuMulai": "2025-05-12T17:10:00",
  "kelas": {
    "id": 1,
    "nama": "Kelas 4"
  },
  "jumlahSoal": 2,
  "tanggal": "2025-05-12",
  "nama": "Kuis ke - 12",
  "waktuSelesai": "2025-05-12T17:20:00"
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

  "pesan": "Akses ditolak ke kuis ini" 

}
```
- akses di tolak karena guru mo coba akses kuis dari kelas lain punya

### Lihat Soal per Topik Pake Halaman
## Endpoint : GET zequiz/soal/page?topikId=11&page=0&size=5

- params
- topikId
- page = for halaman ke berapa (dimuali dari 0)
- size = jumlah soal yang ingin di tampilkan

### response body (Success) :
```json
{
  "content": [
    {
      "id": 23,
      "topik": {
        "id": 11,
        "nama": "gambar",
        "kelas": {
          "id": 1,
          "nama": "Kelas 4"
        }
      },
      "pertanyaan": "hahah",
      "gambar": null,
      "opsiA": "A.hah",
      "opsiB": "B.hah",
      "opsiC": "C.a",
      "opsiD": "D.bzb",
      "jawabanBenar": "D"
    },
    {
      "id": 25,
      "topik": {
        "id": 11,
        "nama": "gambar",
        "kelas": {
          "id": 1,
          "nama": "Kelas 4"
        }
      },
      "pertanyaan": "bab",
      "gambar": null,
      "opsiA": "A.s",
      "opsiB": "B.zz",
      "opsiC": "C.az",
      "opsiD": "D.az",
      "jawabanBenar": "D"
    },
    {
      "id": 26,
      "topik": {
        "id": 11,
        "nama": "gambar",
        "kelas": {
          "id": 1,
          "nama": "Kelas 4"
        }
      },
      "pertanyaan": "tai",
      "gambar": "1746557956771_IMG-20250506-WA0011.jpg",
      "opsiA": "A.hah",
      "opsiB": "B.hehe",
      "opsiC": "C.tata",
      "opsiD": "D.huhu",
      "jawabanBenar": "D"
    },
    {
      "id": 27,
      "topik": {
        "id": 11,
        "nama": "gambar",
        "kelas": {
          "id": 1,
          "nama": "Kelas 4"
        }
      },
      "pertanyaan": "ucup",
      "gambar": "1746559071569_IMG-20250507-WA0001.jpg",
      "opsiA": "A.bab",
      "opsiB": "B.baba",
      "opsiC": "C.haha",
      "opsiD": "D.babab",
      "jawabanBenar": "D"
    },
    {
      "id": 29,
      "topik": {
        "id": 11,
        "nama": "gambar",
        "kelas": {
          "id": 1,
          "nama": "Kelas 4"
        }
      },
      "pertanyaan": "tataat",
      "gambar": null,
      "opsiA": "A.jaha",
      "opsiB": "B.haha",
      "opsiC": "C.yB",
      "opsiD": "D.bB",
      "jawabanBenar": "A"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 5,
    "sort": {
      "empty": true,
      "sorted": false,
      "unsorted": true
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "last": true,
  "totalElements": 5,
  "totalPages": 1,
  "first": true,
  "size": 5,
  "number": 0,
  "sort": {
    "empty": true,
    "sorted": false,
    "unsorted": true
  },
  "numberOfElements": 5,
  "empty": false
}
```
### response body (Faild) :
```json
{

  "pesan": "Akses ditolak ke kuis ini" 

}
```