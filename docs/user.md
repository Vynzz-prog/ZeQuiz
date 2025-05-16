# API SPEC USER

### Register

## Endpoint : POST zequiz/auth/register

- Menyimpan pengguna baru ke database

### request body :
```json
{
  "username":"Budi",
  "kelas": "4",
  "kata_sandi" : "rahasia",
  "konfirmasi_kata_sandi" : "rahasia"
}
```

### response body (Success) :
```json
{
  "pesan" : "Registrasi Berhasil"
}
```

### response body (Faild) :
```json
{
  "pesan" : "Ada bagian yang masih kosong"
}
```
### Login

## Endpoint : Post zequiz/auth/login

- mengotentikasikan pengguna

### request body :
```json
{
  "username":"Budi",
  "kata_sandi" : "rahasia"
}
```

### response body (Success) :
```json
{
  "id": 1,
  "pesan": "Login berhasil",
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJndXJ1MSIsImlhdCI6MTc0NjM2MTU4NSwiZXhwIjoxNzQ2NDQ3OTg1fQ.Zp9ELoIrCHhQZ6O-RKhZSvqffX7Tf9gcx8dFSGyh3KM",
  "role": "GURU",
  "username": "guru1",
  "kelas": "Kelas 4"
}
```

### response body (Faild) :
```json
{
  "pesan": "Username tidak ditemukan // Kata sandi salah"
}
```

## Lihat daftar kuis

### Endpoint : GET zequiz/kuis/kelas/{kelasId}

### Contoh zequiz/kuis/kelas/1

- catatan : guru deng siswa dari kelas lain boleh ambe / lia dpe daftar kalo dpe API betul,
            cuma nanti di FE dpe API for bagian {kelasId}, sistem so tau ni user kelas berapa pas login,
            otomatis dpe API so ta isi sandiri dpe kelasId

### response body (Success) :
```json
[
  {
    "id": 27,
    "namaKuis": "lllll",
    "timer": 30,
    "jumlahSoal": 1,
    "tanggal": "2025-05-10",
    "namaTopik": "coba",
    "waktuMulai": null,
    "waktuSelesai": null
  },
  {
    "id": 29,
    "namaKuis": "Kuis Matematika Bab 2",
    "timer": 120,
    "jumlahSoal": 1,
    "tanggal": "2025-05-11",
    "namaTopik": "gambar",
    "waktuMulai": "2025-05-11T02:00",
    "waktuSelesai": "2025-05-11T04:00"
  }
]
```

### response body (Faild) :
```json
{
  "pesan" : "unauthorized"
}
```

## Lihat detail kuis
### Endpoint : GET zequiz/kuis/{kuisId}

- contoh zequiz/kuis/61

### response body (Success) :
```json
{
    "nama": "Kuis ke - 17",
    "id": 61
}
```

### response body (Faild) :
```json
{
  "pesan" : "unauthorized"
}
```