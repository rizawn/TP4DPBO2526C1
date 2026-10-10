import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Class PeopleMenu merepresentasikan antarmuka pengguna berbasis Java Swing GUI.
 * Mengimplementasikan Single Inheritance dengan mewarisi kelas JFrame (extends JFrame).
 * Menyediakan operasi CRUD lengkap (Create, Read, Update, Delete) dengan dialog konfirmasi,
 * serta memanfaatkan komponen JRadioButton (BONUS +20) untuk atribut Jenis Kelamin.
 * 
 * @author Riza Wahyu Nugraha (2511421)
 */
public class PeopleMenu extends JFrame {
    private JPanel mainPanel;
    private JLabel titleLabel;
    private JTextField idField;
    private JTextField namaField;
    private JTextField tahunLahirField;
    private JComboBox<String> kategoriComboBox;
    private JRadioButton lakiLakiRadio;
    private JRadioButton perempuanRadio;
    private ButtonGroup genderGroup;
    private JButton addUpdateButton;
    private JButton cancelButton;
    private JButton deleteButton;
    private JTable peopleTable;
    private JScrollPane tableScrollPane;

    // Koleksi data penampung objek Person (In-Memory List)
    private ArrayList<Person> listPerson;
    private int selectedIndex = -1;

    /**
     * Inisialisasi antarmuka secara programatis untuk kompatibilitas penuh.
     * Metode ini memastikan program dapat dijalankan baik melalui IntelliJ GUI Designer
     * maupun kompilasi manual via javac tanpa memerlukan byte-code instrumenter khusus.
     */
    private void initCustomUI() {
        mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        mainPanel.setBackground(Color.WHITE);

        // Header / Title
        titleLabel = new JLabel("Data Personil Rumah Sakit", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(25, 60, 110));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Form Panel (Input fields + Action Buttons)
        JPanel formContainer = new JPanel(new BorderLayout(20, 0));
        formContainer.setBackground(Color.WHITE);

        JPanel inputGrid = new JPanel(new GridBagLayout());
        inputGrid.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Inisialisasi komponen form
        idField = new JTextField(20);
        namaField = new JTextField(20);
        tahunLahirField = new JTextField(20);
        kategoriComboBox = new JComboBox<>();
        lakiLakiRadio = new JRadioButton("Laki-laki");
        lakiLakiRadio.setBackground(Color.WHITE);
        perempuanRadio = new JRadioButton("Perempuan");
        perempuanRadio.setBackground(Color.WHITE);

        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        genderPanel.setBackground(Color.WHITE);
        genderPanel.add(lakiLakiRadio);
        genderPanel.add(perempuanRadio);

        String[] labels = { "ID", "Nama", "Tahun Lahir", "Kategori", "Jenis Kelamin" };
        JComponent[] fields = { idField, namaField, tahunLahirField, kategoriComboBox, genderPanel };

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.weightx = 0.0;
            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
            inputGrid.add(lbl, gbc);

            gbc.gridx = 1;
            gbc.weightx = 1.0;
            inputGrid.add(fields[i], gbc);
        }
        formContainer.add(inputGrid, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 0, 10));
        buttonPanel.setBackground(Color.WHITE);
        addUpdateButton = new JButton("Add");
        addUpdateButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        addUpdateButton.setFocusPainted(false);

        cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cancelButton.setFocusPainted(false);

        deleteButton = new JButton("Delete");
        deleteButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        deleteButton.setBackground(new Color(220, 53, 69));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);

        buttonPanel.add(addUpdateButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(deleteButton);

        JPanel buttonWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        buttonWrapper.setBackground(Color.WHITE);
        buttonWrapper.add(buttonPanel);
        formContainer.add(buttonWrapper, BorderLayout.EAST);

        // Center Panel: Form + Table
        JPanel centerPanel = new JPanel(new BorderLayout(0, 15));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(formContainer, BorderLayout.NORTH);

        peopleTable = new JTable();
        peopleTable.setRowHeight(24);
        peopleTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        peopleTable.getTableHeader().setBackground(new Color(240, 243, 248));
        tableScrollPane = new JScrollPane(peopleTable);
        centerPanel.add(tableScrollPane, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
    }

    /**
     * Constructor utama PeopleMenu.
     */
    public PeopleMenu() {
        // Setup UI jika belum terinisialisasi oleh compiler GUI form
        if (mainPanel == null) {
            initCustomUI();
        }
        setContentPane(mainPanel);

        // 1. Inisialisasi listPerson
        listPerson = new ArrayList<>();

        // 2. Isi listPerson dengan data awal
        populateList();

        // 3. Isi tabel personil
        peopleTable.setModel(setTable());

        // 4. Ubah styling title
        if (titleLabel != null) {
            titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));
        }

        // 5. Atur isi combo box
        String[] kategoriData = { "???", "Dokter Spesialis", "Dokter Umum", "Perawat", "Apoteker", "Staf Administrasi" };
        kategoriComboBox.setModel(new DefaultComboBoxModel<>(kategoriData));

        // Inisialisasi ButtonGroup untuk JRadioButton Jenis Kelamin (BONUS +20)
        genderGroup = new ButtonGroup();
        if (lakiLakiRadio != null && perempuanRadio != null) {
            genderGroup.add(lakiLakiRadio);
            genderGroup.add(perempuanRadio);
        }

        // 6. Sembunyikan button delete saat kondisi awal
        deleteButton.setVisible(false);

        // 7. Listener saat tombol add/update ditekan
        addUpdateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedIndex == -1) {
                    insertData();
                } else {
                    updateData();
                }
            }
        });

        // 8. Listener saat tombol delete ditekan (disertai konfirmasi sebelum menghapus)
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Confirmation prompt sesuai modul dan spesifikasi tugas
                int confirm = JOptionPane.showConfirmDialog(
                    null,
                    "Apakah anda yakin akan menghapus data personil ini?",
                    "Konfirmasi Hapus Data",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    deleteData();
                }
            }
        });

        // 9. Listener saat tombol cancel ditekan
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });

        // 10. Listener saat salah satu baris tabel ditekan
        peopleTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Ubah selectedIndex menjadi baris tabel yang diklik
                selectedIndex = peopleTable.getSelectedRow();
                if (selectedIndex >= 0 && selectedIndex < listPerson.size()) {
                    // Ambil nilai dari model tabel
                    String curId = peopleTable.getModel().getValueAt(selectedIndex, 1).toString();
                    String curNama = peopleTable.getModel().getValueAt(selectedIndex, 2).toString();
                    String curTahun = peopleTable.getModel().getValueAt(selectedIndex, 3).toString();
                    String curKategori = peopleTable.getModel().getValueAt(selectedIndex, 4).toString();
                    String curGender = peopleTable.getModel().getValueAt(selectedIndex, 5).toString();

                    // Tampilkan kembali pada input form
                    idField.setText(curId);
                    namaField.setText(curNama);
                    tahunLahirField.setText(curTahun);
                    kategoriComboBox.setSelectedItem(curKategori);

                    // Pemilihan nilai JRadioButton sesuai data baris
                    if ("Laki-laki".equalsIgnoreCase(curGender)) {
                        lakiLakiRadio.setSelected(true);
                    } else if ("Perempuan".equalsIgnoreCase(curGender)) {
                        perempuanRadio.setSelected(true);
                    } else {
                        genderGroup.clearSelection();
                    }

                    // Ubah label tombol Add menjadi Update dan tampilkan tombol Delete
                    addUpdateButton.setText("Update");
                    deleteButton.setVisible(true);
                }
            }
        });
    }

    /**
     * Mengatur model tabel dan memuat seluruh data dari listPerson.
     * @return DefaultTableModel yang berisi baris data personil
     */
    public final DefaultTableModel setTable() {
        // Tentukan kolom tabel
        Object[] cols = { "No", "ID", "Nama", "Tahun Lahir", "Kategori", "Jenis Kelamin" };

        // Buat objek tabel dengan kolom yang sudah dibuat
        DefaultTableModel tmp = new DefaultTableModel(null, cols) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Mencegah editing langsung pada cell tabel
            }
        };

        // Isi tabel dengan data listPerson
        for (int i = 0; i < listPerson.size(); i++) {
            Person p = listPerson.get(i);
            Object[] row = {
                i + 1,
                p.getId(),
                p.getNama(),
                p.getTahunLahir(),
                p.getKategori(),
                p.getJenisKelamin()
            };
            tmp.addRow(row);
        }

        return tmp;
    }

    /**
     * Memasukkan data baru ke dalam list dan memperbarui tabel.
     */
    public void insertData() {
        try {
            // Ambil value dari textfield, combobox, dan radio button
            String id = idField.getText().trim();
            String nama = namaField.getText().trim();
            String tahunStr = tahunLahirField.getText().trim();
            String kategori = kategoriComboBox.getSelectedItem() != null ? kategoriComboBox.getSelectedItem().toString() : "???";

            // Validasi input tidak boleh kosong
            if (id.isEmpty() || nama.isEmpty() || tahunStr.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Semua field harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Validasi pemilihan kategori
            if ("???".equals(kategori)) {
                JOptionPane.showMessageDialog(null, "Silakan pilih kategori yang valid!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Validasi pemilihan radio button Jenis Kelamin
            String jenisKelamin = "";
            if (lakiLakiRadio.isSelected()) {
                jenisKelamin = "Laki-laki";
            } else if (perempuanRadio.isSelected()) {
                jenisKelamin = "Perempuan";
            } else {
                JOptionPane.showMessageDialog(null, "Silakan pilih jenis kelamin!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Validasi tahun lahir harus berupa angka bulat
            int tahunLahir = Integer.parseInt(tahunStr);
            if (tahunLahir < 1900 || tahunLahir > 2026) {
                JOptionPane.showMessageDialog(null, "Tahun lahir harus dalam rentang 1900 - 2026!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Validasi keunikan ID (ID tidak boleh duplikat)
            for (Person p : listPerson) {
                if (p.getId().equalsIgnoreCase(id)) {
                    JOptionPane.showMessageDialog(null, "ID '" + id + "' sudah terdaftar! Gunakan ID lain.", "ID Duplikat", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            // Tambahkan data ke dalam list
            listPerson.add(new Person(id, nama, tahunLahir, kategori, jenisKelamin));

            // Update tabel
            peopleTable.setModel(setTable());

            // Bersihkan form
            clearForm();

            // Feedback notifikasi
            System.out.println("Insert berhasil");
            JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan", "Sukses", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Tahun lahir harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Memperbarui data yang sedang dipilih pada baris tabel.
     */
    public void updateData() {
        if (selectedIndex < 0 || selectedIndex >= listPerson.size()) {
            JOptionPane.showMessageDialog(null, "Pilih baris data terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Ambil data dari form
            String id = idField.getText().trim();
            String nama = namaField.getText().trim();
            String tahunStr = tahunLahirField.getText().trim();
            String kategori = kategoriComboBox.getSelectedItem() != null ? kategoriComboBox.getSelectedItem().toString() : "???";

            // Validasi field kosong
            if (id.isEmpty() || nama.isEmpty() || tahunStr.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Semua field harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Validasi kategori
            if ("???".equals(kategori)) {
                JOptionPane.showMessageDialog(null, "Silakan pilih kategori yang valid!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Validasi jenis kelamin
            String jenisKelamin = "";
            if (lakiLakiRadio.isSelected()) {
                jenisKelamin = "Laki-laki";
            } else if (perempuanRadio.isSelected()) {
                jenisKelamin = "Perempuan";
            } else {
                JOptionPane.showMessageDialog(null, "Silakan pilih jenis kelamin!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Validasi tahun lahir
            int tahunLahir = Integer.parseInt(tahunStr);
            if (tahunLahir < 1900 || tahunLahir > 2026) {
                JOptionPane.showMessageDialog(null, "Tahun lahir harus dalam rentang 1900 - 2026!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Validasi keunikan ID (tidak boleh sama dengan ID orang lain)
            for (int i = 0; i < listPerson.size(); i++) {
                if (i != selectedIndex && listPerson.get(i).getId().equalsIgnoreCase(id)) {
                    JOptionPane.showMessageDialog(null, "ID '" + id + "' sudah terdaftar pada personil lain!", "ID Duplikat", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            // Ubah data personil di list
            Person p = listPerson.get(selectedIndex);
            p.setId(id);
            p.setNama(nama);
            p.setTahunLahir(tahunLahir);
            p.setKategori(kategori);
            p.setJenisKelamin(jenisKelamin);

            // Update tabel
            peopleTable.setModel(setTable());

            // Bersihkan form
            clearForm();

            // Feedback
            System.out.println("Update berhasil");
            JOptionPane.showMessageDialog(null, "Data berhasil diubah", "Sukses", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Tahun lahir harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Menghapus data personil pada indeks baris yang sedang dipilih.
     */
    public void deleteData() {
        if (selectedIndex < 0 || selectedIndex >= listPerson.size()) {
            JOptionPane.showMessageDialog(null, "Pilih baris data terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Hapus data dari list
        listPerson.remove(selectedIndex);

        // Update tabel
        peopleTable.setModel(setTable());

        // Bersihkan form
        clearForm();

        // Feedback
        System.out.println("Delete berhasil");
        JOptionPane.showMessageDialog(null, "Data berhasil dihapus", "Sukses", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Mengosongkan form input dan mengembalikan kondisi tombol ke keadaan awal.
     */
    public void clearForm() {
        // Kosongkan semua textfield dan combo box
        idField.setText("");
        namaField.setText("");
        tahunLahirField.setText("");
        kategoriComboBox.setSelectedIndex(0);

        // Reset pilihan radio button
        if (genderGroup != null) {
            genderGroup.clearSelection();
        }

        // Ubah button "Update" menjadi "Add"
        addUpdateButton.setText("Add");

        // Sembunyikan button delete
        deleteButton.setVisible(false);

        // Ubah selectedIndex menjadi -1 (tidak ada baris yang dipilih)
        selectedIndex = -1;
    }

    /**
     * Mengisi data awal (hardcoded 10 entri) saat program pertama kali dibuka.
     */
    public void populateList() {
        listPerson.add(new Person("MED-001", "dr. Budi Santoso, Sp.A", 1982, "Dokter Spesialis", "Laki-laki"));
        listPerson.add(new Person("MED-002", "dr. Siti Aminah, Sp.PD", 1985, "Dokter Spesialis", "Perempuan"));
        listPerson.add(new Person("MED-003", "dr. Rian Pratama", 1992, "Dokter Umum", "Laki-laki"));
        listPerson.add(new Person("MED-004", "Ns. Dewi Lestari, S.Kep", 1995, "Perawat", "Perempuan"));
        listPerson.add(new Person("MED-005", "Ns. Ahmad Fauzi, S.Kep", 1996, "Perawat", "Laki-laki"));
        listPerson.add(new Person("MED-006", "apt. Nadia Putri, S.Farm", 1993, "Apoteker", "Perempuan"));
        listPerson.add(new Person("MED-007", "apt. Rizky Ramadhan", 1994, "Apoteker", "Laki-laki"));
        listPerson.add(new Person("MED-008", "Maya Indah, S.E.", 1998, "Staf Administrasi", "Perempuan"));
        listPerson.add(new Person("MED-009", "Hendra Wijaya, S.Kom", 1990, "Staf Administrasi", "Laki-laki"));
        listPerson.add(new Person("MED-010", "Clarissa Yunita, A.Md", 1999, "Staf Administrasi", "Perempuan"));
    }

    /**
     * Main method untuk menjalankan aplikasi desktop Swing GUI.
     */
    public static void main(String[] args) {
        // Mengatur Look and Feel sistem untuk tampilan modern
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        // Buat object window
        PeopleMenu menu = new PeopleMenu();

        // Atur ukuran window
        menu.setSize(750, 620);

        // Letakkan window di tengah layar
        menu.setLocationRelativeTo(null);

        // Isi window
        menu.setContentPane(menu.mainPanel);

        // Ubah warna background
        menu.getContentPane().setBackground(Color.WHITE);

        // Berikan judul window
        menu.setTitle("PeopleData - Manajemen Data Personil RS");

        // Tampilkan window
        menu.setVisible(true);

        // Agar program ikut berhenti saat window diclose
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
