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
  "pesan" : "Login Berhasil",
  "token" : "jduydgaydhdwkdjiu",
  "role": "SISWA",
  "username": "Budi",
  "kelas": 4
}
```

### response body (Faild) :
```json
{
  "message": "Username tidak ditemukan",
  "token": null,
  "role": null,
  "username": null,
  "kelas": null,


  "message": "Kata sandi salah",
  "token": null,
  "role": null,
  "username": null,
  "kelas": null
}
```
