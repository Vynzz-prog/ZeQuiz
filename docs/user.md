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
  "pesan": "Login berhasil",
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJndXJ1MiIsImlhdCI6MTc0NjIwMjExMywiZXhwIjoxNzQ2Mjg4NTEzfQ.1h48p6DI_M67YS6yCBph_ZVP_Km1LaficfwAlJK3Gfo",
  "role": "GURU",
  "username": "guru2",
  "kelas": "Kelas 5"
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
    "id": 1,
    "timer": 60,
    "jumlahSoal": 2,
    "tanggal": "2025-04-24",
    "namaTopik": "Perkalian"
  },
  {
    "id": 2,
    "timer": 120,
    "jumlahSoal": 1,
    "tanggal": "2025-04-29",
    "namaTopik": "Perkalian"
  }
]
```

### response body (Faild) :
```json
{
  "pesan" : "unauthorized"
}
```
