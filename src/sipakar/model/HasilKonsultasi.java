package sipakar.model;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class HasilKonsultasi {
    private String namaSiswa;
    private String asalSekolah;
    private String kelas;
    private Date tanggal;
    private Map<String, Double> skorJurusan; // kode -> persentase
    private String rekomendasiUtama;
    private double persenUtama;

    public HasilKonsultasi(String namaSiswa, String asalSekolah, String kelas) {
        this.namaSiswa = namaSiswa;
        this.asalSekolah = asalSekolah;
        this.kelas = kelas;
        this.tanggal = new Date();
        this.skorJurusan = new LinkedHashMap<>();
    }

    public String getNamaSiswa() { return namaSiswa; }
    public String getAsalSekolah() { return asalSekolah; }
    public String getKelas() { return kelas; }
    public Date getTanggal() { return tanggal; }
    public Map<String, Double> getSkorJurusan() { return skorJurusan; }
    public String getRekomendasiUtama() { return rekomendasiUtama; }
    public double getPersenUtama() { return persenUtama; }

    public void setSkorJurusan(Map<String, Double> skor) { this.skorJurusan = skor; }
    public void setRekomendasiUtama(String kode) { this.rekomendasiUtama = kode; }
    public void setPersenUtama(double persen) { this.persenUtama = persen; }
}
