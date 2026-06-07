package sipakar.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class PanelSplash extends JPanel {

    // Warna tema
    static final Color C_BG     = new Color(26, 24, 20);
    static final Color C_ACCENT = new Color(123, 150, 240);
    static final Color C_TEXT   = new Color(245, 242, 237);
    static final Color C_MUTED  = new Color(130, 128, 122);
    static final Color C_BTN    = new Color(245, 242, 237);

    private Runnable onMulai;

    public PanelSplash(Runnable onMulai) {
        this.onMulai = onMulai;
        setBackground(C_BG);
        setLayout(new GridBagLayout());
        buatKomponen();
    }

    private void buatKomponen() {
        JPanel inner = new JPanel();
        inner.setOpaque(false);
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setMaximumSize(new Dimension(500, Integer.MAX_VALUE));

        // Badge
        JLabel badge = new JLabel("✦  SISTEM PAKAR · FORWARD CHAINING");
        badge.setFont(new Font("SansSerif", Font.PLAIN, 11));
        badge.setForeground(C_MUTED);
        badge.setAlignmentX(Component.CENTER_ALIGNMENT);
        badge.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(80, 78, 74), 1, true),
            BorderFactory.createEmptyBorder(6, 18, 6, 18)
        ));

        // Judul besar
        JLabel judul1 = buatLabel("Temukan", 52, Font.PLAIN, C_TEXT);
        JLabel judul2 = buatLabel("Jurusanmu", 52, Font.BOLD, C_ACCENT);

        // Sub
        JTextArea sub = new JTextArea(
            "Jawab 15 pertanyaan singkat dan sistem pakar kami akan\n" +
            "merekomendasikan jurusan kuliah yang paling sesuai\n" +
            "dengan minat, kemampuan, dan kepribadianmu.");
        sub.setEditable(false);
        sub.setOpaque(false);
        sub.setForeground(C_MUTED);
        sub.setFont(new Font("SansSerif", Font.PLAIN, 14));
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);
        sub.setLineWrap(true);
        sub.setWrapStyleWord(true);
        sub.setMaximumSize(new Dimension(440, 100));

        // Badge jurusan
        JPanel jurusanRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 4));
        jurusanRow.setOpaque(false);
        String[] jurs = {"Teknik Informatika","Teknik Sipil","Kedokteran",
                         "Ekonomi","Hukum","Pendidikan","Pertanian"};
        for (String j : jurs) jurusanRow.add(buatChip(j));

        // Tombol mulai
        JButton btnMulai = new JButton("  Mulai Konsultasi  →");
        btnMulai.setFont(new Font("SansSerif", Font.BOLD, 15));
        btnMulai.setBackground(C_BTN);
        btnMulai.setForeground(C_BG);
        btnMulai.setFocusPainted(false);
        btnMulai.setBorderPainted(false);
        btnMulai.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnMulai.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnMulai.setMaximumSize(new Dimension(260, 50));
        btnMulai.setBorder(BorderFactory.createEmptyBorder(12, 32, 12, 32));
        btnMulai.addActionListener(e -> onMulai.run());
        btnMulai.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnMulai.setBackground(new Color(220, 217, 212)); }
            public void mouseExited(MouseEvent e)  { btnMulai.setBackground(C_BTN); }
        });

        // Footer
        JLabel footer = new JLabel("SiPakar v1.0  ·  Sistem Pakar Pemilihan Jurusan Kuliah");
        footer.setFont(new Font("SansSerif", Font.PLAIN, 10));
        footer.setForeground(new Color(60, 58, 55));
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Susun komponen
        inner.add(badge);
        inner.add(Box.createVerticalStrut(28));
        inner.add(judul1);
        inner.add(judul2);
        inner.add(Box.createVerticalStrut(18));
        inner.add(sub);
        inner.add(Box.createVerticalStrut(24));
        inner.add(jurusanRow);
        inner.add(Box.createVerticalStrut(36));
        inner.add(btnMulai);
        inner.add(Box.createVerticalStrut(48));
        inner.add(footer);

        add(inner);
    }

    private JLabel buatLabel(String teks, int size, int style, Color warna) {
        JLabel l = new JLabel(teks);
        l.setFont(new Font("Serif", style, size));
        l.setForeground(warna);
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        return l;
    }

    private JLabel buatChip(String teks) {
        JLabel l = new JLabel(teks);
        l.setFont(new Font("SansSerif", Font.PLAIN, 11));
        l.setForeground(new Color(100, 98, 93));
        l.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(55, 53, 50), 1, true),
            BorderFactory.createEmptyBorder(4, 10, 4, 10)
        ));
        return l;
    }
}
