# SiPakar — Sistem Pakar Pemilihan Jurusan Kuliah

![Java](https://img.shields.io/badge/Java-ED8B00?style=flat&logo=openjdk&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=flat&logo=mysql&logoColor=white)
![NetBeans](https://img.shields.io/badge/NetBeans-1B6AC6?style=flat&logo=apache-netbeans-ide&logoColor=white)
![XAMPP](https://img.shields.io/badge/XAMPP-FB7A24?style=flat&logo=xampp&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-green?style=flat)

Aplikasi desktop berbasis Java Swing yang membantu siswa SMA/SMK menemukan jurusan kuliah paling sesuai melalui 15 pertanyaan terstruktur. Menggunakan metode **forward chaining** dengan sistem pembobotan untuk menghasilkan rekomendasi dari 7 jurusan secara terukur.

---

## Fitur

- 15 pertanyaan dari 4 kategori: minat, kemampuan, nilai rapor, kepribadian
- Mesin inferensi forward chaining + weighted scoring matrix (15x7)
- Rekomendasi 7 jurusan diurutkan berdasarkan persentase kecocokan
- Riwayat konsultasi tersimpan otomatis ke database MySQL
- GUI modern 5 layar dengan navigasi CardLayout yang mulus
- Cetak / salin ringkasan hasil konsultasi

## Jurusan yang direkomendasikan

| Kode | Jurusan |
|------|---------|
| TI | Teknik Informatika / Ilmu Komputer |
| TS | Teknik Sipil / Arsitektur |
| DOK | Kedokteran / Ilmu Kesehatan |
| EKO | Ekonomi / Akuntansi / Manajemen |
| HKM | Ilmu Hukum |
| PEND | Ilmu Pendidikan / Keguruan |
| PER | Pertanian / Perikanan / Kehutanan |

## Tech stack

- **Bahasa**: Java SE 17+
- **GUI**: Java Swing (CardLayout, BoxLayout, BorderLayout)
- **IDE**: NetBeans IDE 21
- **Database**: MySQL 8.x via XAMPP
- **Koneksi DB**: JDBC (mysql-connector-j)
- **Arsitektur**: MVC (Model–View–Controller)

## Struktur project

```
src/sipakar/
├── model/
│   ├── Jurusan.java
│   ├── Pertanyaan.java
│   └── HasilKonsultasi.java
├── controller/
│   └── MesinInferensi.java
├── database/
│   └── KoneksiDB.java
└── view/
    ├── MainFrame.java
    ├── PanelSplash.java
    ├── PanelFormDiri.java
    ├── PanelKuis.java
    └── PanelHasil.java
```

## Cara menjalankan

### Prasyarat
- JDK 17 atau lebih baru
- NetBeans IDE
- XAMPP (MySQL aktif)
- `mysql-connector-j-8.x.x.jar` ditambahkan ke Libraries

### Langkah

1. Clone repo ini
   ```bash
   git clone https://github.com/USERNAME/SiPakar.git
   ```

2. Buka project di NetBeans: **File → Open Project** → pilih folder SiPakar

3. Tambahkan MySQL JDBC driver: klik kanan **Libraries → Add JAR/Folder** → pilih `mysql-connector-j.jar`

4. Buat database di phpMyAdmin:
   ```sql
   CREATE DATABASE db_sipakar;
   ```

5. Set Main Class: klik kanan project → **Properties → Run → Main Class**: `sipakar.view.MainFrame`

6. Jalankan dengan **F6** atau tombol ▶

> Jika MySQL tidak aktif, aplikasi tetap berjalan — hanya fitur simpan riwayat yang dilewati.

## Database

Tabel dibuat otomatis saat aplikasi pertama kali dijalankan.

```sql
CREATE TABLE konsultasi (
    id             INT PRIMARY KEY AUTO_INCREMENT,
    nama_siswa     VARCHAR(100),
    asal_sekolah   VARCHAR(150),
    kelas          VARCHAR(30),
    tanggal        DATETIME DEFAULT CURRENT_TIMESTAMP,
    hasil_jurusan  VARCHAR(100),
    skor_tertinggi DECIMAL(5,2)
);

CREATE TABLE detail_skor (
    id             INT PRIMARY KEY AUTO_INCREMENT,
    id_konsultasi  INT,
    kode_jurusan   VARCHAR(10),
    nama_jurusan   VARCHAR(100),
    persentase     DECIMAL(5,2),
    FOREIGN KEY (id_konsultasi) REFERENCES konsultasi(id)
);
```

## Cara kerja sistem

1. Siswa menjawab 15 pertanyaan
2. Setiap jawaban memberi bobot ke 7 jurusan (nilai 0.0–1.0)
3. Skor diakumulasi menggunakan forward chaining
4. Persentase dihitung: `(skor_jurusan / total_skor) × 100`
5. Jurusan diurutkan dari persentase tertinggi → ditampilkan sebagai rekomendasi

## Tentang

Dibuat untuk **UAS Mata Kuliah Artificial Intelligence**
Prodi Teknologi Informasi — UIN Antasari Banjarmasin
Dosen: Wifda Muna Fatihia, M.Tr.Kom

**Achmad Alpianoor** — 240104040144

## Lisensi

[MIT](LICENSE)
