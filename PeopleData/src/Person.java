/**
 * Class Person merepresentasikan entitas individu pada sistem data rumah sakit.
 * Menerapkan prinsip enkapsulasi dengan atribut private serta metode getter dan setter.
 * 
 * Atribut awal: ID, Nama, Tahun Lahir, Kategori.
 * Atribut tambahan (BONUS +20): Jenis Kelamin (dioperasikan via JRadioButton).
 * 
 * @author Riza Wahyu Nugraha (2511421)
 */
public class Person {
    private String id;
    private String nama;
    private int tahunLahir;
    private String kategori;
    private String jenisKelamin;

    // Default constructor
    public Person() {
        this.id = "";
        this.nama = "";
        this.tahunLahir = 0;
        this.kategori = "";
        this.jenisKelamin = "";
    }

    // Parameterized constructor
    public Person(String id, String nama, int tahunLahir, String kategori, String jenisKelamin) {
        this.id = id;
        this.nama = nama;
        this.tahunLahir = tahunLahir;
        this.kategori = kategori;
        this.jenisKelamin = jenisKelamin;
    }

    // Getter dan Setter untuk id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Getter dan Setter untuk nama
    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    // Getter dan Setter untuk tahunLahir
    public int getTahunLahir() {
        return tahunLahir;
    }

    public void setTahunLahir(int tahunLahir) {
        this.tahunLahir = tahunLahir;
    }

    // Getter dan Setter untuk kategori
    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    // Getter dan Setter untuk jenisKelamin
    public String getJenisKelamin() {
        return jenisKelamin;
    }

    public void setJenisKelamin(String jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }
}
