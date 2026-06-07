package sipakar.model;

public class Jurusan {
    private String kode;
    private String nama;
    private String deskripsi;
    private String[] prospekKerja;
    private double skor;

    public Jurusan(String kode, String nama, String deskripsi, String[] prospekKerja) {
        this.kode = kode;
        this.nama = nama;
        this.deskripsi = deskripsi;
        this.prospekKerja = prospekKerja;
        this.skor = 0;
    }

    public String getKode() { return kode; }
    public String getNama() { return nama; }
    public String getDeskripsi() { return deskripsi; }
    public String[] getProspekKerja() { return prospekKerja; }
    public double getSkor() { return skor; }
    public void setSkor(double skor) { this.skor = skor; }
    public void tambahSkor(double nilai) { this.skor += nilai; }
    public void resetSkor() { this.skor = 0; }
}
