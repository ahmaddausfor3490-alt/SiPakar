package sipakar.view;

import sipakar.controller.MesinInferensi;
import sipakar.database.KoneksiDB;
import sipakar.model.HasilKonsultasi;
import sipakar.model.Jurusan;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel     mainPanel;
    private MesinInferensi mesin;

    // Data sementara
    private String   namaSiswa, asalSekolah, kelasNya;

    public MainFrame() {
        super("SiPakar — Sistem Pakar Pemilihan Jurusan Kuliah");
        mesin = new MesinInferensi();
        initUI();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(720, 560));
        setSize(860, 640);
        setLocationRelativeTo(null); // tengah layar
        setVisible(true);
    }

    private void initUI() {
        cardLayout = new CardLayout();
        mainPanel  = new JPanel(cardLayout);

        // Semua panel didaftarkan ke CardLayout
        mainPanel.add(new PanelSplash(this::keSplashForm),   "splash");
        mainPanel.add(new PanelFormDiri(this::keKuis),        "form");

        // Kuis dan hasil dibuat saat dibutuhkan
        mainPanel.add(new JPanel(), "kuis");
        mainPanel.add(new JPanel(), "hasil");

        add(mainPanel);
        cardLayout.show(mainPanel, "splash");
    }

    /** Splash → Form data diri */
    private void keSplashForm() {
        cardLayout.show(mainPanel, "form");
    }

    /** Form → Kuis */
    private void keKuis(String[] data) {
        namaSiswa   = data[0];
        asalSekolah = data[1];
        kelasNya    = data[2];
        mesin.resetJawaban();

        PanelKuis panelKuis = new PanelKuis(
            mesin.getDaftarPertanyaan(),
            mesin.getJawaban(),
            mesin::setJawaban,      // onJawab
            this::keHasil,          // onSelesai
            () -> cardLayout.show(mainPanel, "form") // onKembali
        );

        mainPanel.remove(mainPanel.getComponent(2));
        mainPanel.add(panelKuis, "kuis", 2);
        cardLayout.show(mainPanel, "kuis");
        mainPanel.revalidate();
    }

    /** Kuis → Loading → Hasil */
    private void keHasil() {
        // Tampilkan loading sebentar dengan SwingWorker
        JPanel loadingPanel = buatPanelLoading();
        mainPanel.remove(mainPanel.getComponent(3));
        mainPanel.add(loadingPanel, "hasil", 3);
        cardLayout.show(mainPanel, "hasil");
        mainPanel.revalidate();

        SwingWorker<HasilKonsultasi, String> worker = new SwingWorker<>() {
            @Override
            protected HasilKonsultasi doInBackground() throws Exception {
                publish("Memuat basis pengetahuan...");
                Thread.sleep(600);
                publish("Menjalankan forward chaining...");
                Thread.sleep(700);
                publish("Menghitung bobot tiap jurusan...");
                Thread.sleep(600);
                publish("Menyusun rekomendasi...");
                Thread.sleep(500);
                return mesin.jalankanInferensi(namaSiswa, asalSekolah, kelasNya);
            }

            @Override
            protected void process(List<String> chunks) {
                if (loadingPanel.getComponentCount() > 0) {
                    Component c = ((JPanel)loadingPanel.getComponent(0)).getComponent(1);
                    if (c instanceof JLabel lbl) lbl.setText(chunks.get(chunks.size()-1));
                }
            }

            @Override
            protected void done() {
                try {
                    HasilKonsultasi hasil = get();
                    List<Jurusan> urutan = mesin.getJurusanUrut();

                    // Simpan ke DB (opsional, tidak crash jika DB mati)
                    try { KoneksiDB.simpanHasil(hasil, urutan); }
                    catch (Exception ex) { System.err.println("[DB] Skip simpan: " + ex.getMessage()); }

                    PanelHasil panelHasil = new PanelHasil(hasil, urutan, () -> {
                        mainPanel.remove(mainPanel.getComponent(3));
                        mainPanel.add(new JPanel(), "hasil", 3);
                        cardLayout.show(mainPanel, "splash");
                        mainPanel.revalidate();
                    });

                    mainPanel.remove(mainPanel.getComponent(3));
                    mainPanel.add(panelHasil, "hasil", 3);
                    cardLayout.show(mainPanel, "hasil");
                    mainPanel.revalidate();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(MainFrame.this,
                        "Terjadi kesalahan: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private JPanel buatPanelLoading() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(26, 24, 20));

        JPanel inner = new JPanel();
        inner.setOpaque(false);
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Menganalisis jawabanmu…");
        title.setFont(new Font("Serif", Font.PLAIN, 26));
        title.setForeground(new Color(245, 242, 237));
        title.setAlignmentX(CENTER_ALIGNMENT);

        JLabel step = new JLabel("Memuat basis pengetahuan...");
        step.setFont(new Font("SansSerif", Font.PLAIN, 13));
        step.setForeground(new Color(130, 128, 122));
        step.setAlignmentX(CENTER_ALIGNMENT);

        JProgressBar bar = new JProgressBar();
        bar.setIndeterminate(true);
        bar.setBackground(new Color(50, 48, 44));
        bar.setForeground(new Color(123, 150, 240));
        bar.setMaximumSize(new Dimension(300, 4));
        bar.setBorderPainted(false);

        inner.add(title);
        inner.add(Box.createVerticalStrut(14));
        inner.add(step);
        inner.add(Box.createVerticalStrut(20));
        inner.add(bar);

        panel.add(inner);
        return panel;
    }

    // ── ENTRY POINT ──
    public static void main(String[] args) {
        // Gunakan FlatLaf atau Nimbus agar tampilan lebih modern
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            // fallback ke default
        }

        // Inisialisasi database (buat tabel jika belum ada)
        try { KoneksiDB.inisialisasiDatabase(); }
        catch (Exception e) { System.err.println("[DB] Skip init: " + e.getMessage()); }

        SwingUtilities.invokeLater(MainFrame::new);
    }
}
