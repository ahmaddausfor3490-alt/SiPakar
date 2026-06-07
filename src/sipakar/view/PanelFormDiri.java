package sipakar.view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.function.Consumer;

public class PanelFormDiri extends JPanel {

    private static final Color C_BG     = new Color(245, 242, 237);
    private static final Color C_CARD   = Color.WHITE;
    private static final Color C_INK    = new Color(26, 24, 20);
    private static final Color C_MUTED  = new Color(107, 104, 96);
    private static final Color C_BORDER = new Color(226, 221, 214);
    private static final Color C_ACCENT = new Color(45, 91, 227);

    private JTextField tfNama;
    private JTextField tfSekolah;
    private JComboBox<String> cbKelas;
    private Consumer<String[]> onLanjut;

    public PanelFormDiri(Consumer<String[]> onLanjut) {
        this.onLanjut = onLanjut;
        setBackground(C_BG);
        setLayout(new GridBagLayout());
        buatKomponen();
    }

    private void buatKomponen() {
        // Card putih di tengah
        JPanel card = new JPanel();
        card.setBackground(C_CARD);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
            new ShadowBorder(),
            BorderFactory.createEmptyBorder(36, 40, 36, 40)
        ));
        card.setMaximumSize(new Dimension(460, Integer.MAX_VALUE));

        // Label langkah
        JLabel stepLabel = buatLabel("LANGKAH 1 DARI 3", 10, Font.BOLD, C_ACCENT);
        stepLabel.setAlignmentX(LEFT_ALIGNMENT);
        stepLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));

        JLabel judul = new JLabel("Data diri kamu");
        judul.setFont(new Font("Serif", Font.PLAIN, 26));
        judul.setForeground(C_INK);
        judul.setAlignmentX(LEFT_ALIGNMENT);

        JLabel sub = buatLabel("Isi informasi berikut sebelum memulai sesi konsultasi.", 13, Font.PLAIN, C_MUTED);
        sub.setAlignmentX(LEFT_ALIGNMENT);
        sub.setBorder(BorderFactory.createEmptyBorder(4, 0, 28, 0));

        // Field nama
        card.add(stepLabel);
        card.add(judul);
        card.add(sub);
        card.add(buatFieldGroup("Nama lengkap", tfNama = buatTextField("Contoh: Budi Santoso")));
        card.add(Box.createVerticalStrut(16));
        card.add(buatFieldGroup("Asal sekolah", tfSekolah = buatTextField("Contoh: SMAN 1 Banjarmasin")));
        card.add(Box.createVerticalStrut(16));

        // Kelas combo
        cbKelas = new JComboBox<>(new String[]{
            "-- Pilih kelas --", "Kelas 10", "Kelas 11", "Kelas 12", "Sudah lulus SMA/SMK"
        });
        cbKelas.setFont(new Font("SansSerif", Font.PLAIN, 13));
        cbKelas.setBackground(C_BG);
        cbKelas.setForeground(C_INK);
        cbKelas.setBorder(BorderFactory.createLineBorder(C_BORDER, 1));
        cbKelas.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        card.add(buatFieldGroup("Kelas", cbKelas));
        card.add(Box.createVerticalStrut(24));

        // Tombol lanjut
        JButton btnLanjut = new JButton("Lanjut ke Pertanyaan  →");
        btnLanjut.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnLanjut.setBackground(C_INK);
        btnLanjut.setForeground(new Color(245, 242, 237));
        btnLanjut.setFocusPainted(false);
        btnLanjut.setBorderPainted(false);
        btnLanjut.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLanjut.setAlignmentX(LEFT_ALIGNMENT);
        btnLanjut.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        btnLanjut.setBorder(BorderFactory.createEmptyBorder(12, 0, 12, 0));
        btnLanjut.addActionListener(e -> validasiDanLanjut());

        card.add(btnLanjut);

        add(card);
    }

    private void validasiDanLanjut() {
        String nama    = tfNama.getText().trim();
        String sekolah = tfSekolah.getText().trim();
        String kelas   = (String) cbKelas.getSelectedItem();

        if (nama.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Nama lengkap harus diisi!", "Validasi", JOptionPane.WARNING_MESSAGE);
            tfNama.requestFocus();
            return;
        }
        if (cbKelas.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this,
                "Pilih kelas terlebih dahulu!", "Validasi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        onLanjut.accept(new String[]{nama, sekolah, kelas});
    }

    private JPanel buatFieldGroup(String labelTeks, JComponent field) {
        JPanel grp = new JPanel();
        grp.setOpaque(false);
        grp.setLayout(new BoxLayout(grp, BoxLayout.Y_AXIS));
        grp.setAlignmentX(LEFT_ALIGNMENT);

        JLabel lbl = buatLabel(labelTeks, 12, Font.BOLD, new Color(26, 24, 20));
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        lbl.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));

        field.setAlignmentX(LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        grp.add(lbl);
        grp.add(field);
        return grp;
    }

    private JTextField buatTextField(String placeholder) {
        JTextField tf = new JTextField();
        tf.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tf.setForeground(new Color(26, 24, 20));
        tf.setBackground(C_BG);
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(C_BORDER, 1, true),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        // Placeholder teks
        tf.setText(placeholder);
        tf.setForeground(C_MUTED);
        tf.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (tf.getText().equals(placeholder)) { tf.setText(""); tf.setForeground(new Color(26,24,20)); }
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                if (tf.getText().isEmpty()) { tf.setText(placeholder); tf.setForeground(C_MUTED); }
            }
        });
        return tf;
    }

    private JLabel buatLabel(String teks, int size, int style, Color warna) {
        JLabel l = new JLabel(teks);
        l.setFont(new Font("SansSerif", style, size));
        l.setForeground(warna);
        return l;
    }

    /** Border efek bayangan sederhana */
    static class ShadowBorder extends AbstractBorder {
        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(26, 24, 20, 14));
            g2.fillRoundRect(x+4, y+6, w-8, h-8, 20, 20);
            g2.setColor(new Color(26, 24, 20, 8));
            g2.fillRoundRect(x+2, y+3, w-4, h-4, 20, 20);
            g2.dispose();
        }
        public Insets getBorderInsets(Component c) { return new Insets(8, 6, 10, 6); }
    }
}
