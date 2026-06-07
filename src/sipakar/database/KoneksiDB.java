package sipakar.database;

import java.sql.*;

public class KoneksiDB {

    private static final String HOST     = "localhost";
    private static final String PORT     = "3306";
    private static final String DATABASE = "db_sipakar";
    private static final String USER     = "root";
    private static final String PASSWORD = ""; // sesuaikan password XAMPP kamu

    private static final String URL =
        "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE
        + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Makassar";

    private static Connection connection = null;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("[DB] Koneksi berhasil ke " + DATABASE);
            }
        } catch (ClassNotFoundException e) {
            System.err.println("[DB] Driver MySQL tidak ditemukan: " + e.getMessage());
            System.err.println("     Pastikan mysql-connector-java-8.x.jar ada di Libraries project.");
        } catch (SQLException e) {
            System.err.println("[DB] Gagal koneksi: " + e.getMessage());
            System.err.println("     Pastikan XAMPP MySQL berjalan dan database '" + DATABASE + "' sudah dibuat.");
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("[DB] Koneksi ditutup.");
            }
        } catch (SQLException e) {
            System.err.println("[DB] Gagal menutup koneksi: " + e.getMessage());
        }
    }

    /** Buat tabel jika belum ada (jalankan sekali saat startup) */
    public static void inisialisasiDatabase() {
        String sqlKonsultasi = """
            CREATE TABLE IF NOT EXISTS konsultasi (
                id              INT PRIMARY KEY AUTO_INCREMENT,
                nama_siswa      VARCHAR(100) NOT NULL,
                asal_sekolah    VARCHAR(150),
                kelas           VARCHAR(30),
                tanggal         DATETIME DEFAULT CURRENT_TIMESTAMP,
                hasil_jurusan   VARCHAR(100),
                skor_tertinggi  DECIMAL(5,2)
            )
            """;

        String sqlDetail = """
            CREATE TABLE IF NOT EXISTS detail_skor (
                id              INT PRIMARY KEY AUTO_INCREMENT,
                id_konsultasi   INT,
                kode_jurusan    VARCHAR(10),
                nama_jurusan    VARCHAR(100),
                persentase      DECIMAL(5,2),
                FOREIGN KEY (id_konsultasi) REFERENCES konsultasi(id)
            )
            """;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sqlKonsultasi);
            stmt.execute(sqlDetail);
            System.out.println("[DB] Tabel siap.");
        } catch (SQLException e) {
            System.err.println("[DB] Gagal membuat tabel: " + e.getMessage());
        }
    }

    /** Simpan hasil konsultasi dan detail skor */
    public static boolean simpanHasil(sipakar.model.HasilKonsultasi hasil,
                                      java.util.List<sipakar.model.Jurusan> urutanJurusan) {
        String sqlMain   = "INSERT INTO konsultasi (nama_siswa, asal_sekolah, kelas, hasil_jurusan, skor_tertinggi) VALUES (?,?,?,?,?)";
        String sqlDetail = "INSERT INTO detail_skor (id_konsultasi, kode_jurusan, nama_jurusan, persentase) VALUES (?,?,?,?)";

        try (Connection conn = getConnection();
             PreparedStatement psMain   = conn.prepareStatement(sqlMain, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement psDetail = conn.prepareStatement(sqlDetail)) {

            psMain.setString(1, hasil.getNamaSiswa());
            psMain.setString(2, hasil.getAsalSekolah());
            psMain.setString(3, hasil.getKelas());
            psMain.setString(4, hasil.getRekomendasiUtama());
            psMain.setDouble(5, hasil.getPersenUtama());
            psMain.executeUpdate();

            ResultSet rs = psMain.getGeneratedKeys();
            if (rs.next()) {
                int idKonsultasi = rs.getInt(1);
                for (sipakar.model.Jurusan j : urutanJurusan) {
                    double persen = hasil.getSkorJurusan().getOrDefault(j.getKode(), 0.0);
                    psDetail.setInt(1, idKonsultasi);
                    psDetail.setString(2, j.getKode());
                    psDetail.setString(3, j.getNama());
                    psDetail.setDouble(4, persen);
                    psDetail.addBatch();
                }
                psDetail.executeBatch();
            }
            System.out.println("[DB] Hasil konsultasi disimpan.");
            return true;

        } catch (SQLException e) {
            System.err.println("[DB] Gagal menyimpan: " + e.getMessage());
            return false;
        }
    }
}
