package sipakar.model;

public class Pertanyaan {
    private int id;
    private String kategori;
    private String teks;
    private String[] pilihan;
    private double[][] bobotJawaban; // [indexPilihan][indexJurusan]

    public Pertanyaan(int id, String kategori, String teks,
                      String[] pilihan, double[][] bobotJawaban) {
        this.id = id;
        this.kategori = kategori;
        this.teks = teks;
        this.pilihan = pilihan;
        this.bobotJawaban = bobotJawaban;
    }

    public int getId() { return id; }
    public String getKategori() { return kategori; }
    public String getTeks() { return teks; }
    public String[] getPilihan() { return pilihan; }

    // Ambil bobot untuk pilihan tertentu, semua jurusan
    // urutan jurusan: TI, TS, DOK, EKO, HKM, PEND, PER
    public double[] getBobotPilihan(int indexPilihan) {
        return bobotJawaban[indexPilihan];
    }
}
