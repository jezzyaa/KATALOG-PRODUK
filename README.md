# Katalog Produk

Aplikasi desktop berbasis **JavaFX** untuk mengelola katalog produk dengan database **SQLite**. Pengguna dapat menambah produk, melihatnya sebagai kartu katalog, mengubah nama, harga, dan foto produk di halaman terpisah, serta menghapus produk. Data tersimpan permanen di file database, sehingga masih ada setelah aplikasi ditutup.

# Tampilan

<img width="1000" height="760" alt="Katalog Produk UPI Store 11_18_2026 12_35_26 PM" src="https://github.com/user-attachments/assets/d6c08f8b-a4f1-4763-b01f-df8f10d84021" />
<img width="1000" height="760" alt="Katalog Produk UPI Store 11_18_2026 12_35_48 PM" src="https://github.com/user-attachments/assets/2476860d-55b9-4c68-9a63-95c4bb603c6e" />

https://github.com/user-attachments/assets/fcd032a0-2dfa-4721-af50-cea604a72b05

## Fitur

- **Tambah produk** (nama, harga, foto) dan langsung tersimpan ke database.
- **Katalog kartu** yang menampilkan foto, nama, harga, dan ID produk.
- **Edit produk di halaman terpisah**: ubah nama, harga, dan foto, dengan *preview* gambar yang berubah langsung saat foto dipilih.
- **Hapus produk** dengan dialog konfirmasi Ya/Tidak.
- **Data permanen** di file SQLite (`data/katalog.db`).

## Pra-Syarat

| Komponen | Versi |
|---|---|
| Java (JDK) | 17 atau lebih baru |
| JavaFX | 21 |
| Database | SQLite lewat `sqlite-jdbc` 3.49.1.0 |
| Build tool | Maven (`javafx-maven-plugin` 0.0.8) |

## Cara Menjalankan

### Lewat NetBeans
1. Buka folder `KatalogProdukUp` sebagai proyek Maven (`File > Open Project`).
2. Klik **Run**.

### Lewat terminal
```bash
git clone <(https://github.com/jezzyaa/KATALOG-PRODUK.git)>
cd KatalogProdukUp
mvn clean javafx:run
```

## Halaman dan Alur

```
Halaman Katalog (tambah + lihat)
   └─ klik "Edit / Hapus" pada kartu ─→ Halaman Edit (menerima objek Produk)
                                          ├─ Simpan Perubahan ─┐
                                          ├─ Hapus Produk ─────┼─→ kembali ke Katalog + pesan
                                          └─ Kembali ──────────┘
```

- **Katalog**: form di bagian atas untuk menambah produk, kartu-kartu produk di bawahnya.
- **Update**: dibuka dari tombol **Edit / Hapus** pada kartu. Berisi nama, harga, dan foto produk yang dipilih.
- Setelah menyimpan perubahan atau menghapus, aplikasi kembali ke katalog dengan pesan hasilnya.

## Database

File database dibuat otomatis saat aplikasi pertama kali dijalankan di `data/katalog.db` (folder `data/` ikut dibuat jika belum ada).

```sql
CREATE TABLE IF NOT EXISTS produk (
  id    INTEGER PRIMARY KEY,
  nama  TEXT    NOT NULL CHECK(length(trim(nama)) > 0),
  harga INTEGER NOT NULL CHECK(harga > 0),
  foto  TEXT    NOT NULL CHECK(length(trim(foto)) > 0)
)
```

| Kolom | Isi |
|---|---|
| `id` | Identitas produk, terisi otomatis oleh SQLite |
| `nama` | Nama produk |
| `harga` | Harga rupiah bulat (misalnya `10000`) |
| `foto` | Nama file gambar, misalnya `susu.png` (gambarnya sendiri ada di `resources/image`) |

- Folder `data/` berada di luar `target/`, sehingga `mvn clean` **tidak** menghapus database.
- Untuk membuat cadangan, salin file `data/katalog.db`. Menghapus file itu berarti menghapus semua data.

## Aturan Validasi

Data diperiksa di tiga lapis: constructor `Produk`, controller (yang menampilkan pesan), dan `CHECK` di database.

| Input | Aturan | Pesan jika ditolak |
|---|---|---|
| Nama produk | Tidak boleh kosong atau hanya spasi | `Nama produk wajib diisi.` |
| Harga | Harus lebih dari nol | `Harga harus lebih dari nol.` |
| Harga | Harus angka bulat | `Harga harus angka bulat, contoh 12000 atau 12.000.` |

## Struktur Proyek

```
KatalogProduk/
├── pom.xml
├── data/                          # katalog.db dibuat otomatis saat pertama dijalankan
└── src/main/
    ├── java/
    │   ├── main/
    │   │   ├── MainApp.java       # titik masuk; menyiapkan database lalu membuka katalog
    │   │   └── Navigator.java     # perpindahan halaman (katalog <-> edit)
    │   ├── model/
    │   │   └── Produk.java        # atribut private, constructor, getter, validasi
    │   ├── db/
    │   │   └── Database.java      # lokasi file, koneksi JDBC, pembuatan tabel
    │   ├── data/
    │   │   └── ProdukRepository.java  # semua SQL: add, getAll, update, delete
    │   ├── controller/
    │   │   ├── KatalogController.java # form tambah + katalog
    │   │   └── EditController.java    # edit (nama, harga, foto) dan hapus
    │   └── util/
    │       └── GambarProduk.java  # memuat foto produk + daftar nama foto
    └── resources/
        ├── view/
        │   ├── katalog.fxml
        │   ├── edit.fxml
        │   └── styles/app.css     
        └── image/                 # roti.png, susu.png, kopi.png
```

## Tema Tampilan

Seluruh tampilan diatur di `src/main/resources/view/styles/app.css`.

| Peran | Warna |
|---|---|
| Dinding krem (latar) | `#FFF3D6` |
| Tomat (judul, tombol utama) | `#D43D2C` |
| Coklat (teks) | `#5B3A29` |
| Mentega (tombol sekunder) | `#FFD36B` |
| Berry (tombol hapus) | `#A32035` |

## Catatan Desain

- **Repository**: seluruh perintah SQL ada di `ProdukRepository`, sehingga controller tidak berisi SQL.
- **`PreparedStatement`** (tanda `?`) dipakai untuk semua perintah, sehingga aman dari *SQL injection* dan nama ber-apostrof tetap tersimpan benar.
- **`try (...)`** menutup koneksi dan perintah secara otomatis. Error dilempar sebagai `SQLException` ke controller dan ditampilkan sebagai pesan, tanpa `System.exit`.
- **`Database.prepareDatabase()`** dipanggil sebelum halaman pertama dimuat, supaya tabel sudah ada saat katalog membaca data.
- **`Navigator`** memakai satu `Scene` dan hanya mengganti isinya (`setRoot`). Halaman edit menerima objek `Produk` yang dipilih, bukan data yang ditulis langsung di kode.
- Setelah tambah, ubah, atau hapus, katalog **membaca ulang dari database**, sehingga tampilan selalu sama dengan isi data.

## Skenario Pengujian

| Skenario | Hasil yang diharapkan |
|---|---|
| Tambah `Roti Cokelat`, harga `10000`, foto `roti.png` | Muncul pesan sukses dan satu kartu dengan harga `Rp10.000` |
| Tambah produk dengan harga `12.000` | Diterima, kartu menampilkan `Rp12.000` |
| Tambah dengan nama kosong, harga `0`, atau harga `abc` | Ditolak dengan pesan kesalahan, tidak ada kartu baru |
| Klik **Edit / Hapus** pada satu kartu | Halaman Edit terbuka dengan data dan preview foto produk itu |
| Ganti foto di halaman Edit | Preview berubah langsung, tetapi belum tersimpan sampai klik Simpan Perubahan |
| Ubah nama, harga, dan foto lalu **Simpan Perubahan** | Kembali ke katalog, kartu menampilkan data baru, ID tetap |
| Klik **Hapus Produk** lalu pilih **No** | Tetap di halaman Edit, data tidak terhapus |
| Klik **Hapus Produk** lalu pilih **Yes** | Kembali ke katalog, produk hilang, jumlah produk berkurang |
| Tutup aplikasi lalu jalankan lagi | Semua perubahan masih ada |
