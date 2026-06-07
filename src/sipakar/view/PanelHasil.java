package sipakar.view;

import sipakar.model.HasilKonsultasi;
import sipakar.model.Jurusan;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class PanelHasil extends JPanel {

    private static final Color C_BG      = new Color(245, 242, 237);
    private static final Color C_HEADER  = new Color(26, 24, 20);
    private static final Color C_CARD    = Color.WHITE;
    private static final Color C_INK     = new Color(26, 24, 20);
    private static final Color C_MUTED   = new Color(107, 104, 96);
    private static final Color C_BORDER  = new Color(226, 221, 214);
    private static final Color C_ACCENT  = new Color(45, 91, 227);
    private static final Color C_ACCENT_L= new Color(235, 240, 255);
    private static final Color C_GREEN   = new Color(123, 227, 160);

    private HasilKonsultasi hasil;
    private List<Jurusan>   urutanJurusan;
    private Map<String, Jurusan> jurusanMap;
    private Runnable onUlang;

    public PanelHasil(HasilKonsultasi hasil, List<Jurusan> urutanJurusan, Runnable onUlang) {
        this.hasil         = hasil;
        this.urutanJurusan = urutanJurusan;
        this.onUlang       = onUlang;

        jurusanMap = new HashMap<>();
        for (Jurusan j : urutanJurusan) jurusanMap.put(j.getKode(), j);

        setBackground(C_BG);
        setLayout(new BorderLayout());
        buatKomponen();
    }

    private void buatKomponen() {
        // ── SCROLL WRAPPER ──
        JPanel konten = new JPanel();
        konten.setBackground(C_BG);
        konten.setLayout(new BoxLayout(konten, BoxLayout.Y_AXIS));

        konten.add(buatHeader());
        konten.add(buatBody());

        JScrollPane scroll = new JScrollPane(konten);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
    }

    // ── HEADER GELAP ────────────────────────
    private JPanel buatHeader() {
        JPanel h = new JPanel(new BorderLayout());
        h.setBackground(C_HEADER);
        h.setBorder(BorderFactory.createEmptyBorder(32, 0, 48, 0));

        JPanel inner = new JPanel();
        inner.setOpaque(false);
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));

        Jurusan top = urutanJurusan.get(0);
        double persen = hasil.getSkorJurusan().getOrDefault(top.getKode(), 0.0);

        JLabel tag = new JLabel("✦  Hasil Konsultasi");
        tag.setFont(new Font("SansSerif", Font.PLAIN, 11));
        tag.setForeground(new Color(120, 118, 113));
        tag.setAlignmentX(CENTER_ALIGNMENT);
        tag.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(55, 53, 50), 1, true),
            BorderFactory.createEmptyBorder(4, 14, 4, 14)
        ));

        JLabel nama = new JLabel(hasil.getNamaSiswa());
        nama.setFont(new Font("SansSerif", Font.PLAIN, 12));
        nama.setForeground(new Color(120, 118, 113));
        nama.setAlignmentX(CENTER_ALIGNMENT);

        JLabel jurusanTop = new JLabel("<html><center>" + top.getNama() + "</center></html>");
        jurusanTop.setFont(new Font("Serif", Font.PLAIN, 28));
        jurusanTop.setForeground(new Color(245, 242, 237));
        jurusanTop.setAlignmentX(CENTER_ALIGNMENT);
        jurusanTop.setHorizontalAlignment(SwingConstants.CENTER);
        jurusanTop.setMaximumSize(new Dimension(600, 100));

        JLabel matchLabel = new JLabel("Kecocokan tertinggi: ");
        matchLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        matchLabel.setForeground(new Color(130, 128, 122));
        JLabel matchPct = new JLabel(String.format("%.1f%%", persen));
        matchPct.setFont(new Font("SansSerif", Font.BOLD, 13));
        matchPct.setForeground(C_GREEN);
        JPanel matchRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 0));
        matchRow.setOpaque(false);
        matchRow.add(matchLabel);
        matchRow.add(matchPct);
        matchRow.setAlignmentX(CENTER_ALIGNMENT);

        inner.add(tag);
        inner.add(Box.createVerticalStrut(12));
        inner.add(nama);
        inner.add(Box.createVerticalStrut(8));
        inner.add(jurusanTop);
        inner.add(Box.createVerticalStrut(10));
        inner.add(matchRow);

        h.add(inner, BorderLayout.CENTER);
        return h;
    }

    // ── BODY ────────────────────────────────
    private JPanel buatBody() {
        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(BorderFactory.createEmptyBorder(0, 40, 40, 40));
        body.setAlignmentX(CENTER_ALIGNMENT);

        // Overlay card (negatif margin efek — digeser naik)
        body.add(Box.createVerticalStrut(-20));
        body.add(buatTopCard());
        body.add(Box.createVerticalStrut(20));
        body.add(buatLabelSeksi("Rekomendasi lainnya"));
        body.add(Box.createVerticalStrut(10));
        body.add(buatListLainnya());
        body.add(Box.createVerticalStrut(24));
        body.add(buatTombolAksi());

        return body;
    }

    private JPanel buatTopCard() {
        Jurusan top = urutanJurusan.get(0);
        double persen = hasil.getSkorJurusan().getOrDefault(top.getKode(), 0.0);

        JPanel card = new JPanel();
        card.setBackground(C_CARD);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(C_ACCENT, 2, true),
            BorderFactory.createEmptyBorder(24, 28, 24, 28)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        card.setAlignmentX(LEFT_ALIGNMENT);

        JLabel badge = new JLabel("  ✦  REKOMENDASI UTAMA  ");
        badge.setFont(new Font("SansSerif", Font.BOLD, 11));
        badge.setForeground(Color.WHITE);
        badge.setBackground(C_ACCENT);
        badge.setOpaque(true);
        badge.setBorder(BorderFactory.createEmptyBorder(4, 12, 4, 12));
        badge.setAlignmentX(LEFT_ALIGNMENT);

        JLabel namaJurusan = new JLabel(top.getNama());
        namaJurusan.setFont(new Font("Serif", Font.PLAIN, 22));
        namaJurusan.setForeground(C_INK);
        namaJurusan.setAlignmentX(LEFT_ALIGNMENT);

        JLabel deskLabel = new JLabel("<html><body style='width:460px;font-size:11pt'>" + top.getDeskripsi().replace("\n","<br>") + "</body></html>");
        deskLabel.setForeground(C_MUTED);
        deskLabel.setAlignmentX(LEFT_ALIGNMENT);

        // Bar progress
        JPanel barRow = new JPanel(new BorderLayout(12, 0));
        barRow.setOpaque(false);
        barRow.setAlignmentX(LEFT_ALIGNMENT);
        barRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JProgressBar bar = new JProgressBar(0, 100);
        bar.setValue((int) persen);
        bar.setStringPainted(false);
        bar.setBackground(C_BORDER);
        bar.setForeground(C_ACCENT);
        bar.setPreferredSize(new Dimension(0, 8));
        bar.setBorder(BorderFactory.createEmptyBorder());

        JLabel pctLabel = new JLabel(String.format("%.1f%%", persen));
        pctLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        pctLabel.setForeground(C_ACCENT);

        barRow.add(bar, BorderLayout.CENTER);
        barRow.add(pctLabel, BorderLayout.EAST);

        // Prospek kerja
        JPanel prospekRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        prospekRow.setOpaque(false);
        prospekRow.setAlignmentX(LEFT_ALIGNMENT);
        for (String p : top.getProspekKerja()) {
            JLabel chip = new JLabel(p);
            chip.setFont(new Font("SansSerif", Font.PLAIN, 11));
            chip.setForeground(C_ACCENT);
            chip.setBackground(C_ACCENT_L);
            chip.setOpaque(true);
            chip.setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 10));
            prospekRow.add(chip);
        }

        card.add(badge);
        card.add(Box.createVerticalStrut(14));
        card.add(namaJurusan);
        card.add(Box.createVerticalStrut(8));
        card.add(deskLabel);
        card.add(Box.createVerticalStrut(16));
        card.add(barRow);
        card.add(Box.createVerticalStrut(12));
        card.add(prospekRow);

        return card;
    }

    private JPanel buatListLainnya() {
        JPanel list = new JPanel();
        list.setOpaque(false);
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setAlignmentX(LEFT_ALIGNMENT);

        for (int i = 1; i < urutanJurusan.size(); i++) {
            Jurusan j = urutanJurusan.get(i);
            double persen = hasil.getSkorJurusan().getOrDefault(j.getKode(), 0.0);

            JPanel row = new JPanel(new BorderLayout(12, 0));
            row.setBackground(C_CARD);
            row.setBorder(BorderFactory.createCompoundBorder(
                new PanelFormDiri.ShadowBorder(),
                BorderFactory.createEmptyBorder(12, 16, 12, 16)
            ));
            row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
            row.setAlignmentX(LEFT_ALIGNMENT);

            JLabel rankLabel = new JLabel(String.valueOf(i + 1));
            rankLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
            rankLabel.setForeground(C_MUTED);
            rankLabel.setHorizontalAlignment(SwingConstants.CENTER);
            rankLabel.setPreferredSize(new Dimension(32, 32));
            rankLabel.setBackground(C_BG);
            rankLabel.setOpaque(true);
            rankLabel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

            JPanel info = new JPanel();
            info.setOpaque(false);
            info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));

            JLabel namaLabel = new JLabel(j.getNama());
            namaLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
            namaLabel.setForeground(C_INK);

            JProgressBar miniBar = new JProgressBar(0, 100);
            miniBar.setValue((int) persen);
            miniBar.setStringPainted(false);
            miniBar.setBackground(C_BORDER);
            miniBar.setForeground(new Color(C_ACCENT.getRed(), C_ACCENT.getGreen(), C_ACCENT.getBlue(), 130));
            miniBar.setPreferredSize(new Dimension(0, 4));
            miniBar.setBorder(BorderFactory.createEmptyBorder());
            miniBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 4));

            info.add(namaLabel);
            info.add(Box.createVerticalStrut(6));
            info.add(miniBar);

            JLabel pctLbl = new JLabel(String.format("%.1f%%", persen));
            pctLbl.setFont(new Font("SansSerif", Font.BOLD, 13));
            pctLbl.setForeground(C_MUTED);

            row.add(rankLabel, BorderLayout.WEST);
            row.add(info, BorderLayout.CENTER);
            row.add(pctLbl, BorderLayout.EAST);

            list.add(row);
            list.add(Box.createVerticalStrut(8));
        }
        return list;
    }

    private JPanel buatTombolAksi() {
        JPanel row = new JPanel(new GridLayout(1, 2, 10, 0));
        row.setOpaque(false);
        row.setAlignmentX(LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JButton btnCetak = new JButton("🖨  Cetak / Simpan PDF");
        btnCetak.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnCetak.setBackground(C_INK);
        btnCetak.setForeground(new Color(245, 242, 237));
        btnCetak.setFocusPainted(false);
        btnCetak.setBorderPainted(false);
        btnCetak.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCetak.addActionListener(e -> cetakHasil());

        JButton btnUlang = new JButton("↩  Konsultasi Ulang");
        btnUlang.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnUlang.setBackground(C_CARD);
        btnUlang.setForeground(C_INK);
        btnUlang.setFocusPainted(false);
        btnUlang.setBorder(BorderFactory.createLineBorder(C_BORDER, 1));
        btnUlang.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnUlang.addActionListener(e -> onUlang.run());

        row.add(btnCetak);
        row.add(btnUlang);
        return row;
    }

    private void cetakHasil() {
        // Buat dialog ringkasan teks untuk dicetak / disalin
        Jurusan top = urutanJurusan.get(0);
        StringBuilder sb = new StringBuilder();
        sb.append("======================================\n");
        sb.append("  HASIL KONSULTASI — SIPAKAR\n");
        sb.append("======================================\n\n");
        sb.append("Nama       : ").append(hasil.getNamaSiswa()).append("\n");
        sb.append("Sekolah    : ").append(hasil.getAsalSekolah()).append("\n");
        sb.append("Kelas      : ").append(hasil.getKelas()).append("\n");
        sb.append("Tanggal    : ").append(new SimpleDateFormat("dd MMM yyyy HH:mm").format(hasil.getTanggal())).append("\n\n");
        sb.append("── REKOMENDASI JURUSAN ──\n\n");
        for (int i = 0; i < urutanJurusan.size(); i++) {
            Jurusan j = urutanJurusan.get(i);
            double p  = hasil.getSkorJurusan().getOrDefault(j.getKode(), 0.0);
            sb.append(String.format("%d. %-45s %.1f%%\n", i+1, j.getNama(), p));
        }
        sb.append("\n── DESKRIPSI UTAMA ──\n\n");
        sb.append(top.getNama()).append("\n");
        sb.append(top.getDeskripsi()).append("\n\n");
        sb.append("Prospek kerja: ").append(String.join(", ", top.getProspekKerja())).append("\n");
        sb.append("\n======================================\n");
        sb.append("  SiPakar v1.0 — Sistem Pakar Jurusan\n");
        sb.append("======================================\n");

        JTextArea ta = new JTextArea(sb.toString());
        ta.setEditable(false);
        ta.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane sp = new JScrollPane(ta);
        sp.setPreferredSize(new Dimension(550, 400));

        JOptionPane.showMessageDialog(this, sp,
            "Ringkasan Hasil Konsultasi — Salin / Print",
            JOptionPane.PLAIN_MESSAGE);
    }

    private JLabel buatLabelSeksi(String teks) {
        JLabel l = new JLabel(teks.toUpperCase());
        l.setFont(new Font("SansSerif", Font.BOLD, 11));
        l.setForeground(new Color(168, 165, 159));
        l.setAlignmentX(LEFT_ALIGNMENT);
        return l;
    }
}
