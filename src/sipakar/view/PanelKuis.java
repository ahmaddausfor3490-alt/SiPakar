package sipakar.view;

import sipakar.model.Pertanyaan;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.function.BiConsumer;

public class PanelKuis extends JPanel {

    private static final Color C_BG       = new Color(245, 242, 237);
    private static final Color C_CARD     = Color.WHITE;
    private static final Color C_INK      = new Color(26, 24, 20);
    private static final Color C_MUTED    = new Color(107, 104, 96);
    private static final Color C_BORDER   = new Color(226, 221, 214);
    private static final Color C_ACCENT   = new Color(45, 91, 227);
    private static final Color C_ACCENT_L = new Color(235, 240, 255);
    private static final Color C_DISABLED = new Color(190, 187, 182);

    private final List<Pertanyaan>           pertanyaan;
    private final int[]                      jawaban;
    private final BiConsumer<Integer,Integer> onJawab;
    private final Runnable                   onSelesai;
    private final Runnable                   onKembali;
    private int currentIndex = 0;

    // Header
    private JLabel       lblHeaderKat;
    private JLabel       lblHeaderProgress;
    private JProgressBar progressBar;

    // Body (diupdate tiap soal)
    private JLabel  lblBadge;
    private JLabel  lblNomor;
    private JLabel  lblSoal;
    private JPanel  panelPilihan;
    private JButton btnKembali;
    private JButton btnLanjut;

    public PanelKuis(List<Pertanyaan> pertanyaan, int[] jawaban,
                     BiConsumer<Integer,Integer> onJawab,
                     Runnable onSelesai, Runnable onKembali) {
        this.pertanyaan = pertanyaan;
        this.jawaban    = jawaban;
        this.onJawab    = onJawab;
        this.onSelesai  = onSelesai;
        this.onKembali  = onKembali;

        setLayout(new BorderLayout());
        setBackground(C_BG);

        add(buatHeader(), BorderLayout.NORTH);
        add(buatBody(),   BorderLayout.CENTER);

        tampilkanSoal(0);
    }

    // ═══════════════════════════════════════
    //  HEADER
    // ═══════════════════════════════════════
    private JPanel buatHeader() {
        JPanel h = new JPanel(new BorderLayout(16, 0));
        h.setBackground(Color.WHITE);
        h.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, C_BORDER),
            BorderFactory.createEmptyBorder(12, 20, 12, 20)
        ));

        JLabel logo = new JLabel("SiPakar");
        logo.setFont(new Font("Serif", Font.BOLD, 18));
        logo.setForeground(C_INK);
        logo.setPreferredSize(new Dimension(100, 30));

        JPanel progArea = new JPanel(new BorderLayout(0, 5));
        progArea.setOpaque(false);

        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);
        lblHeaderKat = new JLabel("Minat");
        lblHeaderKat.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblHeaderKat.setForeground(C_MUTED);
        lblHeaderProgress = new JLabel("1 / " + pertanyaan.size());
        lblHeaderProgress.setFont(new Font("SansSerif", Font.BOLD, 11));
        lblHeaderProgress.setForeground(C_ACCENT);
        topRow.add(lblHeaderKat,      BorderLayout.WEST);
        topRow.add(lblHeaderProgress, BorderLayout.EAST);

        progressBar = new JProgressBar(0, pertanyaan.size());
        progressBar.setValue(1);
        progressBar.setStringPainted(false);
        progressBar.setBackground(C_BORDER);
        progressBar.setForeground(C_ACCENT);
        progressBar.setPreferredSize(new Dimension(0, 6));
        progressBar.setBorder(null);

        progArea.add(topRow,      BorderLayout.NORTH);
        progArea.add(progressBar, BorderLayout.SOUTH);

        h.add(logo,     BorderLayout.WEST);
        h.add(progArea, BorderLayout.CENTER);
        return h;
    }

    // ═══════════════════════════════════════
    //  BODY  — pakai BorderLayout langsung,
    //          BUKAN scroll pane
    // ═══════════════════════════════════════
    private JPanel buatBody() {
        // Wrapper abu-abu
        JPanel outer = new JPanel(new GridBagLayout());
        outer.setBackground(C_BG);
        outer.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        // Card putih
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(C_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(C_BORDER, 1, true),
            BorderFactory.createEmptyBorder(24, 28, 16, 28)
        ));

        // ── Bagian atas card (badge + nomor + soal + pilihan)
        JPanel atas = new JPanel();
        atas.setOpaque(false);
        atas.setLayout(new BoxLayout(atas, BoxLayout.Y_AXIS));

        lblBadge = new JLabel("Minat");
        lblBadge.setFont(new Font("SansSerif", Font.BOLD, 11));
        lblBadge.setOpaque(true);
        lblBadge.setBackground(new Color(238, 237, 254));
        lblBadge.setForeground(new Color(60, 52, 137));
        lblBadge.setBorder(BorderFactory.createEmptyBorder(4, 12, 4, 12));
        lblBadge.setAlignmentX(LEFT_ALIGNMENT);

        lblNomor = new JLabel("Pertanyaan 1 dari " + pertanyaan.size());
        lblNomor.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblNomor.setForeground(C_MUTED);
        lblNomor.setAlignmentX(LEFT_ALIGNMENT);
        lblNomor.setBorder(BorderFactory.createEmptyBorder(10, 0, 6, 0));

        lblSoal = new JLabel("...");
        lblSoal.setFont(new Font("Serif", Font.PLAIN, 17));
        lblSoal.setForeground(C_INK);
        lblSoal.setAlignmentX(LEFT_ALIGNMENT);
        lblSoal.setBorder(BorderFactory.createEmptyBorder(0, 0, 18, 0));

        panelPilihan = new JPanel();
        panelPilihan.setOpaque(false);
        panelPilihan.setLayout(new BoxLayout(panelPilihan, BoxLayout.Y_AXIS));
        panelPilihan.setAlignmentX(LEFT_ALIGNMENT);

        atas.add(lblBadge);
        atas.add(lblNomor);
        atas.add(lblSoal);
        atas.add(panelPilihan);

        // ── Bagian bawah card (tombol navigasi)
        JPanel bawah = new JPanel(new BorderLayout());
        bawah.setOpaque(false);
        bawah.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, C_BORDER),
            BorderFactory.createEmptyBorder(14, 0, 4, 0)
        ));

        btnKembali = buatTombol("← Kembali", false);
        btnLanjut  = buatTombol("Lanjut →", true);
        btnKembali.addActionListener(e -> navigasi(-1));
        btnLanjut .addActionListener(e -> navigasi( 1));

        bawah.add(btnKembali, BorderLayout.WEST);
        bawah.add(btnLanjut,  BorderLayout.EAST);

        card.add(atas,  BorderLayout.CENTER);
        card.add(bawah, BorderLayout.SOUTH);

        // GridBagConstraints agar card mengisi area tengah
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill    = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets  = new Insets(0, 0, 0, 0);
        outer.add(card, gbc);

        return outer;
    }

    // ═══════════════════════════════════════
    //  TAMPILKAN SOAL
    // ═══════════════════════════════════════
    public void tampilkanSoal(int index) {
        currentIndex = index;
        Pertanyaan q = pertanyaan.get(index);

        // Update header
        progressBar.setValue(index + 1);
        lblHeaderProgress.setText((index + 1) + " / " + pertanyaan.size());
        lblHeaderKat.setText(q.getKategori());

        // Update badge
        lblBadge.setText(q.getKategori());
        setWarnaBadge(q.getKategori());

        // Update teks
        lblNomor.setText("Pertanyaan " + (index + 1) + " dari " + pertanyaan.size());
        lblSoal.setText("<html><body style='width:460px'>" + q.getTeks() + "</body></html>");

        // Bangun ulang pilihan
        panelPilihan.removeAll();
        char[] huruf = {'A','B','C','D','E','F'};

        for (int i = 0; i < q.getPilihan().length; i++) {
            final int idx = i;

            // Setiap baris pilihan adalah JPanel
            JPanel baris = new JPanel(new BorderLayout(12, 0));
            baris.setBackground(C_BG);
            baris.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
            baris.setCursor(new Cursor(Cursor.HAND_CURSOR));
            baris.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(C_BORDER, 1, true),
                BorderFactory.createEmptyBorder(10, 14, 10, 14)
            ));

            JLabel lHuruf = new JLabel(String.valueOf(huruf[i]),
                                       SwingConstants.CENTER);
            lHuruf.setFont(new Font("SansSerif", Font.BOLD, 12));
            lHuruf.setOpaque(true);
            lHuruf.setBackground(C_BORDER);
            lHuruf.setForeground(C_MUTED);
            lHuruf.setPreferredSize(new Dimension(28, 28));

            JLabel lTeks = new JLabel(q.getPilihan()[i]);
            lTeks.setFont(new Font("SansSerif", Font.PLAIN, 13));
            lTeks.setForeground(C_INK);

            baris.add(lHuruf, BorderLayout.WEST);
            baris.add(lTeks,  BorderLayout.CENTER);

            // Tandai jika sudah pernah dipilih
            if (jawaban[index] == i) dipilih(baris, lHuruf, lTeks);

            // Mouse handler
            MouseAdapter ma = new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) {
                    // reset semua
                    for (Component c : panelPilihan.getComponents()) {
                        if (c instanceof JPanel p && p.getComponentCount() >= 2) {
                            resetBaris(p,
                                (JLabel) p.getComponent(0),
                                (JLabel) p.getComponent(1));
                        }
                    }
                    dipilih(baris, lHuruf, lTeks);
                    jawaban[currentIndex] = idx;
                    onJawab.accept(currentIndex, idx);
                    aktifLanjut(true);
                }
                @Override public void mouseEntered(MouseEvent e) {
                    if (jawaban[currentIndex] != idx) {
                        baris.setBackground(C_ACCENT_L);
                        baris.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(C_ACCENT, 1, true),
                            BorderFactory.createEmptyBorder(10, 14, 10, 14)));
                    }
                }
                @Override public void mouseExited(MouseEvent e) {
                    if (jawaban[currentIndex] != idx) {
                        baris.setBackground(C_BG);
                        baris.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(C_BORDER, 1, true),
                            BorderFactory.createEmptyBorder(10, 14, 10, 14)));
                    }
                }
            };

            // Pasang handler ke baris dan semua child
            baris.addMouseListener(ma);
            lHuruf.addMouseListener(ma);
            lTeks.addMouseListener(ma);

            panelPilihan.add(baris);
            panelPilihan.add(Box.createRigidArea(new Dimension(0, 8)));
        }

        // Navigasi
        btnKembali.setVisible(index > 0);
        aktifLanjut(jawaban[index] >= 0);
        btnLanjut.setText(index == pertanyaan.size() - 1 ? "Lihat Hasil ✓" : "Lanjut →");

        panelPilihan.revalidate();
        panelPilihan.repaint();
        revalidate();
        repaint();
    }

    // ═══════════════════════════════════════
    //  HELPER
    // ═══════════════════════════════════════
    private void navigasi(int arah) {
        if (arah > 0) {
            if (jawaban[currentIndex] < 0) {
                JOptionPane.showMessageDialog(this,
                    "Silakan pilih salah satu jawaban terlebih dahulu.",
                    "Belum dijawab", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (currentIndex < pertanyaan.size() - 1) tampilkanSoal(currentIndex + 1);
            else onSelesai.run();
        } else {
            if (currentIndex > 0) tampilkanSoal(currentIndex - 1);
            else onKembali.run();
        }
    }

    private void dipilih(JPanel baris, JLabel huruf, JLabel teks) {
        baris.setBackground(C_ACCENT_L);
        baris.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(C_ACCENT, 2, true),
            BorderFactory.createEmptyBorder(10, 14, 10, 14)));
        huruf.setBackground(C_ACCENT);
        huruf.setForeground(Color.WHITE);
        teks.setForeground(C_ACCENT);
        baris.repaint();
    }

    private void resetBaris(JPanel baris, JLabel huruf, JLabel teks) {
        baris.setBackground(C_BG);
        baris.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(C_BORDER, 1, true),
            BorderFactory.createEmptyBorder(10, 14, 10, 14)));
        huruf.setBackground(C_BORDER);
        huruf.setForeground(C_MUTED);
        teks.setForeground(C_INK);
        baris.repaint();
    }

    private void aktifLanjut(boolean aktif) {
        btnLanjut.setEnabled(aktif);
        btnLanjut.setBackground(aktif ? C_INK : C_DISABLED);
        btnLanjut.setForeground(aktif ? new Color(245,242,237) : new Color(160,158,154));
    }

    private JButton buatTombol(String teks, boolean primer) {
        JButton b = new JButton(teks);
        b.setFont(new Font("SansSerif", Font.BOLD, 13));
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        if (primer) {
            b.setBackground(C_INK);
            b.setForeground(new Color(245, 242, 237));
            b.setBorderPainted(false);
            b.setBorder(BorderFactory.createEmptyBorder(10, 22, 10, 22));
        } else {
            b.setBackground(C_BG);
            b.setForeground(C_MUTED);
            b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(C_BORDER, 1, true),
                BorderFactory.createEmptyBorder(10, 22, 10, 22)));
        }
        return b;
    }

    private void setWarnaBadge(String kat) {
        switch (kat) {
            case "Kemampuan"   -> { lblBadge.setBackground(new Color(225,245,238)); lblBadge.setForeground(new Color(15,110,86));  }
            case "Nilai Rapor" -> { lblBadge.setBackground(new Color(250,238,218)); lblBadge.setForeground(new Color(133,79,11));  }
            case "Kepribadian" -> { lblBadge.setBackground(new Color(250,236,231)); lblBadge.setForeground(new Color(153,60,29));  }
            default            -> { lblBadge.setBackground(new Color(238,237,254)); lblBadge.setForeground(new Color(60,52,137)); }
        }
    }
}
