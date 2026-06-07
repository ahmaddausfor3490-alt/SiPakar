package sipakar.controller;

import sipakar.model.Jurusan;
import sipakar.model.Pertanyaan;
import sipakar.model.HasilKonsultasi;

import java.util.*;

public class MesinInferensi {

    // ── URUTAN JURUSAN (indeks kolom bobot): TI=0, TS=1, DOK=2, EKO=3, HKM=4, PEND=5, PER=6
    private static final String[] KODE = {"TI","TS","DOK","EKO","HKM","PEND","PER"};

    private List<Jurusan>     daftarJurusan;
    private List<Pertanyaan>  daftarPertanyaan;
    private int[]             jawaban; // index pilihan tiap pertanyaan (-1 = belum dijawab)

    public MesinInferensi() {
        inisialisasiJurusan();
        inisialisasiPertanyaan();
        jawaban = new int[daftarPertanyaan.size()];
        Arrays.fill(jawaban, -1);
    }

    // ══════════════════════════════════════
    //  BASIS PENGETAHUAN — JURUSAN
    // ══════════════════════════════════════
    private void inisialisasiJurusan() {
        daftarJurusan = new ArrayList<>();
        daftarJurusan.add(new Jurusan("TI",
            "Teknik Informatika / Ilmu Komputer",
            "Mempelajari pengembangan perangkat lunak, kecerdasan buatan,\njaringan, dan sistem informasi.",
            new String[]{"Programmer","Data Scientist","UI/UX Designer","Cyber Security","System Analyst"}));
        daftarJurusan.add(new Jurusan("TS",
            "Teknik Sipil / Arsitektur",
            "Merancang dan membangun infrastruktur seperti gedung,\njembatan, jalan, dan kawasan perumahan.",
            new String[]{"Arsitek","Kontraktor","Konsultan Bangunan","Perencana Kota"}));
        daftarJurusan.add(new Jurusan("DOK",
            "Kedokteran / Ilmu Kesehatan",
            "Belajar ilmu medis dan kesehatan untuk merawat pasien.\nMemerlukan dedikasi tinggi dan berdampak langsung.",
            new String[]{"Dokter Umum","Dokter Spesialis","Apoteker","Ahli Gizi"}));
        daftarJurusan.add(new Jurusan("EKO",
            "Ekonomi / Akuntansi / Manajemen",
            "Memahami sistem keuangan, bisnis, dan manajemen organisasi.\nMembuka banyak peluang di dunia korporasi dan wirausaha.",
            new String[]{"Akuntan","Analis Keuangan","Manajer Bisnis","Wirausahawan"}));
        daftarJurusan.add(new Jurusan("HKM",
            "Ilmu Hukum",
            "Mempelajari sistem hukum, peraturan, dan keadilan.\nCocok bagi yang suka membaca dan membela hak orang lain.",
            new String[]{"Pengacara","Hakim","Notaris","Konsultan Hukum"}));
        daftarJurusan.add(new Jurusan("PEND",
            "Ilmu Pendidikan / Keguruan",
            "Mempersiapkan diri menjadi pendidik berkualitas.\nBerdampak besar pada generasi penerus bangsa.",
            new String[]{"Guru","Dosen","Instruktur","Konselor Pendidikan"}));
        daftarJurusan.add(new Jurusan("PER",
            "Pertanian / Perikanan / Kehutanan",
            "Ilmu tentang budidaya tanaman, hewan, dan pengelolaan\nsumber daya alam untuk ketahanan pangan.",
            new String[]{"Agronomi","Penyuluh Pertanian","Peneliti","Konsultan Agribisnis"}));
    }

    // ══════════════════════════════════════
    //  BASIS ATURAN — 15 PERTANYAAN + BOBOT
    //  Kolom bobot: [TI, TS, DOK, EKO, HKM, PEND, PER]
    // ══════════════════════════════════════
    private void inisialisasiPertanyaan() {
        daftarPertanyaan = new ArrayList<>();

        // Q1
        daftarPertanyaan.add(new Pertanyaan(1,"Minat",
            "Mata pelajaran apa yang paling kamu sukai di sekolah?",
            new String[]{"Matematika / Fisika","Biologi / Kimia",
                         "Ekonomi / Akuntansi","Bahasa / Sastra / PKn",
                         "Pertanian / Lingkungan Hidup"},
            new double[][]{
                {1.0, 0.9, 0.5, 0.7, 0.2, 0.5, 0.3},
                {0.2, 0.2, 1.0, 0.2, 0.1, 0.4, 0.8},
                {0.3, 0.1, 0.1, 1.0, 0.5, 0.3, 0.2},
                {0.2, 0.1, 0.2, 0.4, 0.9, 0.8, 0.1},
                {0.1, 0.3, 0.5, 0.2, 0.1, 0.3, 1.0}
            }));

        // Q2
        daftarPertanyaan.add(new Pertanyaan(2,"Minat",
            "Kegiatan apa yang paling kamu nikmati di waktu luang?",
            new String[]{"Coding / main game komputer",
                         "Menggambar / desain bangunan",
                         "Berdagang / atur keuangan",
                         "Merawat / membantu orang sakit",
                         "Berkebun / memelihara hewan"},
            new double[][]{
                {1.0, 0.4, 0.1, 0.3, 0.1, 0.3, 0.1},
                {0.3, 1.0, 0.1, 0.2, 0.1, 0.2, 0.2},
                {0.3, 0.1, 0.1, 1.0, 0.4, 0.2, 0.2},
                {0.1, 0.1, 1.0, 0.1, 0.2, 0.5, 0.2},
                {0.1, 0.2, 0.3, 0.1, 0.1, 0.2, 1.0}
            }));

        // Q3
        daftarPertanyaan.add(new Pertanyaan(3,"Minat",
            "Profesi impian kamu di masa depan?",
            new String[]{"Programmer / Data Scientist",
                         "Arsitek / Insinyur Sipil",
                         "Dokter / Apoteker",
                         "Pengusaha / Akuntan",
                         "Guru / Dosen",
                         "Petani modern / Peneliti pertanian"},
            new double[][]{
                {1.0, 0.3, 0.1, 0.3, 0.1, 0.2, 0.1},
                {0.4, 1.0, 0.1, 0.2, 0.1, 0.2, 0.2},
                {0.2, 0.1, 1.0, 0.1, 0.1, 0.4, 0.3},
                {0.3, 0.2, 0.1, 1.0, 0.4, 0.2, 0.2},
                {0.3, 0.2, 0.3, 0.3, 0.4, 1.0, 0.3},
                {0.1, 0.2, 0.3, 0.2, 0.1, 0.3, 1.0}
            }));

        // Q4
        daftarPertanyaan.add(new Pertanyaan(4,"Kemampuan",
            "Seberapa mudah kamu memahami logika dan algoritma?",
            new String[]{"Sangat mudah","Cukup mudah","Biasa saja","Sulit"},
            new double[][]{
                {1.0, 0.7, 0.5, 0.6, 0.5, 0.5, 0.3},
                {0.7, 0.6, 0.4, 0.5, 0.4, 0.5, 0.3},
                {0.4, 0.4, 0.3, 0.4, 0.4, 0.4, 0.4},
                {0.1, 0.3, 0.3, 0.3, 0.4, 0.4, 0.5}
            }));

        // Q5
        daftarPertanyaan.add(new Pertanyaan(5,"Kemampuan",
            "Kemampuan berhitung dan analisis data kamu?",
            new String[]{"Sangat baik","Baik","Cukup","Kurang"},
            new double[][]{
                {1.0, 0.9, 0.6, 0.9, 0.3, 0.5, 0.4},
                {0.7, 0.7, 0.5, 0.7, 0.4, 0.5, 0.4},
                {0.4, 0.5, 0.4, 0.5, 0.4, 0.5, 0.4},
                {0.1, 0.3, 0.3, 0.3, 0.5, 0.5, 0.5}
            }));

        // Q6
        daftarPertanyaan.add(new Pertanyaan(6,"Kemampuan",
            "Kemampuan menggambar atau membuat sketsa desain?",
            new String[]{"Sangat baik","Baik","Cukup","Kurang"},
            new double[][]{
                {0.4, 1.0, 0.3, 0.3, 0.2, 0.3, 0.2},
                {0.4, 0.8, 0.3, 0.3, 0.2, 0.3, 0.3},
                {0.4, 0.5, 0.3, 0.3, 0.3, 0.3, 0.3},
                {0.5, 0.2, 0.4, 0.4, 0.4, 0.4, 0.5}
            }));

        // Q7
        daftarPertanyaan.add(new Pertanyaan(7,"Kemampuan",
            "Kemampuan komunikasi dan berbicara di depan umum?",
            new String[]{"Sangat baik","Baik","Cukup","Kurang"},
            new double[][]{
                {0.4, 0.5, 0.7, 0.7, 1.0, 1.0, 0.4},
                {0.4, 0.5, 0.6, 0.6, 0.8, 0.8, 0.4},
                {0.5, 0.4, 0.5, 0.5, 0.5, 0.6, 0.4},
                {0.6, 0.4, 0.3, 0.3, 0.2, 0.3, 0.5}
            }));

        // Q8
        daftarPertanyaan.add(new Pertanyaan(8,"Nilai Rapor",
            "Rata-rata nilai Matematika kamu di rapor?",
            new String[]{"90 - 100  (Sangat baik)","80 - 89  (Baik)",
                         "70 - 79  (Cukup)","Di bawah 70"},
            new double[][]{
                {1.0, 0.9, 0.6, 0.8, 0.3, 0.5, 0.4},
                {0.7, 0.7, 0.5, 0.6, 0.4, 0.5, 0.4},
                {0.4, 0.4, 0.4, 0.4, 0.4, 0.4, 0.4},
                {0.1, 0.2, 0.3, 0.3, 0.5, 0.4, 0.5}
            }));

        // Q9
        daftarPertanyaan.add(new Pertanyaan(9,"Nilai Rapor",
            "Rata-rata nilai Biologi / IPA kamu di rapor?",
            new String[]{"90 - 100  (Sangat baik)","80 - 89  (Baik)",
                         "70 - 79  (Cukup)","Di bawah 70"},
            new double[][]{
                {0.2, 0.2, 1.0, 0.2, 0.1, 0.3, 0.9},
                {0.3, 0.3, 0.8, 0.3, 0.2, 0.4, 0.7},
                {0.4, 0.4, 0.5, 0.4, 0.3, 0.4, 0.5},
                {0.5, 0.5, 0.2, 0.5, 0.5, 0.4, 0.2}
            }));

        // Q10
        daftarPertanyaan.add(new Pertanyaan(10,"Nilai Rapor",
            "Jurusan SMA / SMK kamu saat ini?",
            new String[]{"IPA","IPS","SMK Teknik","SMK Pertanian","SMK Kesehatan"},
            new double[][]{
                {0.9, 0.9, 1.0, 0.4, 0.2, 0.5, 0.7},
                {0.3, 0.2, 0.2, 1.0, 0.9, 0.6, 0.2},
                {0.8, 0.8, 0.2, 0.3, 0.1, 0.3, 0.3},
                {0.2, 0.3, 0.4, 0.3, 0.1, 0.3, 1.0},
                {0.2, 0.1, 0.9, 0.2, 0.1, 0.4, 0.3}
            }));

        // Q11
        daftarPertanyaan.add(new Pertanyaan(11,"Kepribadian",
            "Cara kerja yang paling nyaman bagimu?",
            new String[]{"Sendiri, fokus pada detail",
                         "Tim kecil, brainstorming bersama",
                         "Banyak orang, interaksi sosial",
                         "Di alam / lapangan terbuka"},
            new double[][]{
                {0.9, 0.5, 0.6, 0.5, 0.7, 0.3, 0.3},
                {0.7, 0.7, 0.6, 0.6, 0.6, 0.6, 0.5},
                {0.2, 0.4, 0.7, 0.7, 0.8 , 1.0, 0.4},
                {0.1, 0.5, 0.2, 0.2, 0.1, 0.2, 1.0}
            }));

        // Q12
        daftarPertanyaan.add(new Pertanyaan(12,"Kepribadian",
            "Ketika ada masalah, kamu lebih suka?",
            new String[]{"Analisis data dulu sebelum memutuskan",
                         "Bertanya dan diskusi dengan orang lain",
                         "Langsung coba berbagai solusi",
                         "Riset dan membaca referensi dulu"},
            new double[][]{
                {1.0, 0.7, 0.6, 0.7, 0.7, 0.5, 0.4},
                {0.3, 0.4, 0.5, 0.5, 0.6, 0.8, 0.4},
                {0.7, 0.8, 0.5, 0.5, 0.4, 0.5, 0.7},
                {0.6, 0.5, 0.8, 0.5, 0.9, 0.7, 0.5}
            }));

        // Q13
        daftarPertanyaan.add(new Pertanyaan(13,"Kepribadian",
            "Seberapa peduli kamu terhadap lingkungan dan alam?",
            new String[]{"Sangat peduli, ingin berkontribusi langsung",
                         "Peduli, tapi bukan prioritas karir",
                         "Biasa saja"},
            new double[][]{
                {0.3, 0.5, 0.5, 0.3, 0.4, 0.5, 1.0},
                {0.6, 0.6, 0.6, 0.6, 0.5, 0.6, 0.5},
                {0.7, 0.5, 0.5, 0.6, 0.6, 0.5, 0.2}
            }));

        // Q14
        daftarPertanyaan.add(new Pertanyaan(14,"Kepribadian",
            "Kamu lebih tertarik pada hal yang bersifat?",
            new String[]{"Teknis dan logis",
                         "Kreatif dan estetis",
                         "Sosial dan membantu orang",
                         "Bisnis dan menghasilkan uang"},
            new double[][]{
                {1.0, 0.8, 0.5, 0.6, 0.5, 0.4, 0.4},
                {0.4, 0.9, 0.3, 0.4, 0.3, 0.5, 0.3},
                {0.2, 0.2, 0.8, 0.4, 0.8, 1.0, 0.4},
                {0.5, 0.4, 0.3, 1.0, 0.6, 0.3, 0.4}
            }));

        // Q15
        daftarPertanyaan.add(new Pertanyaan(15,"Minat",
            "Apakah kamu bersedia bekerja di lapangan / luar ruangan?",
            new String[]{"Ya, sangat suka bekerja di lapangan",
                         "Tidak masalah, fleksibel",
                         "Lebih suka di dalam ruangan"},
            new double[][]{
                {0.2, 0.6, 0.3, 0.2, 0.2, 0.3, 1.0},
                {0.5, 0.6, 0.5, 0.5, 0.5, 0.6, 0.6},
                {0.9, 0.5, 0.6, 0.7, 0.8, 0.6, 0.2}
            }));
    }

    // ══════════════════════════════════════
    //  FORWARD CHAINING
    // ══════════════════════════════════════
    public HasilKonsultasi jalankanInferensi(String nama, String sekolah, String kelas) {
        // Reset skor semua jurusan
        for (Jurusan j : daftarJurusan) j.resetSkor();

        // Akumulasi bobot dari setiap jawaban
        for (int i = 0; i < daftarPertanyaan.size(); i++) {
            if (jawaban[i] < 0) continue;
            double[] bobot = daftarPertanyaan.get(i).getBobotPilihan(jawaban[i]);
            for (int j = 0; j < daftarJurusan.size(); j++) {
                daftarJurusan.get(j).tambahSkor(bobot[j]);
            }
        }

        // Hitung total skor
        double total = 0;
        for (Jurusan j : daftarJurusan) total += j.getSkor();

        // Buat HasilKonsultasi
        HasilKonsultasi hasil = new HasilKonsultasi(nama, sekolah, kelas);
        Map<String, Double> skorMap = new LinkedHashMap<>();
        for (Jurusan j : daftarJurusan) {
            double persen = total > 0 ? Math.round((j.getSkor() / total) * 10000.0) / 100.0 : 0;
            skorMap.put(j.getKode(), persen);
        }
        hasil.setSkorJurusan(skorMap);

        // Cari rekomendasi utama
        Jurusan terbaik = daftarJurusan.stream()
            .max(Comparator.comparingDouble(Jurusan::getSkor))
            .orElse(daftarJurusan.get(0));
        hasil.setRekomendasiUtama(terbaik.getNama());
        hasil.setPersenUtama(skorMap.getOrDefault(terbaik.getKode(), 0.0));

        return hasil;
    }

    /** Urutkan jurusan dari skor tertinggi ke terendah */
    public List<Jurusan> getJurusanUrut() {
        List<Jurusan> urut = new ArrayList<>(daftarJurusan);
        urut.sort((a, b) -> Double.compare(b.getSkor(), a.getSkor()));
        return urut;
    }

    // Getter
    public List<Pertanyaan> getDaftarPertanyaan() { return daftarPertanyaan; }
    public List<Jurusan>    getDaftarJurusan()    { return daftarJurusan; }
    public int[]            getJawaban()          { return jawaban; }
    public void             setJawaban(int idx, int val) { jawaban[idx] = val; }
    public void             resetJawaban() { Arrays.fill(jawaban, -1); }
}
