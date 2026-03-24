import java.time.LocalDate; // buat nampilin tanggal sekarang bisa ga bisa input tanggalan setelah tanggal sekarang
import java.util.ArrayList; // buat anuin list dalam array
import java.util.List; // buat nyimpen beberapa varibael dalam list
import java.util.Scanner; // buat nyimpen langsung ke variabel tertentu

public class App {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in); //untuk nge panggil string di fungsi lain


        // ini buat login sebelum masuk ke menu utama
        System.out.println("=== SISTEM MANAJEMEN MAHASISWA ===");
        System.out.print("Masukkan Nama: ");
        String nama_in = sc.nextLine(); // mengambil input teks dari pengguna dan menyimpannya ke dalam variabel nama_in
        System.out.print("Masukkan NIM/ID: ");
        String id_in = sc.nextLine(); // mengambil input teks dari pengguna dan menyimpannya ke dalam variabel id_in
        System.out.println("==================================");

        // Instansiasi sesuai diagram
        Data_Pengguna mhs = new Mahasiswa_Aktif(id_in, nama_in, id_in + "@student.ac.id", "Pass123", "Mahasiswa", id_in, LocalDate.now(), true);
        
        mhs.login_user(); //pemanggilan method (fungsi) dari "mhs" atau dari class "mahasiswa"

        boolean berjalan = true; // make boolean biar pas kode atau user milih opsi misal opsi 1, ngga langsung out atau selesai.. ini buat fungsi switch
        while (berjalan) {
            System.out.println("\n+-------------------------------------------+");
            System.out.println("|           M E N U  U T A M A              |");
            System.out.println("+-------------------------------------------+");
            System.out.println("| [1] Catat Transaksi Keuangan              |");
            System.out.println("| [2] Lihat Dashboard (Tugas & Saldo)       |");
            System.out.println("| [3] Buat Laporan Bulanan                  |");
            System.out.println("| [0] Keluar / Selesai                      |");
            System.out.println("+-------------------------------------------+");
            System.out.print(">>> Pilih Opsi anda: ");
            
            int opsi = sc.nextInt(); // input opsi dan simpan di variabel "opsi"
            sc.nextLine();  // sc ini scan kalo di java.. Digunakan untuk mengambil input teks (String) kayak nama dan Deskripsi

            switch (opsi) {
                case 1:
                    System.out.print("Jumlah Rp: ");
                    double nominal = sc.nextDouble(); sc.nextLine(); // input jumlah duit yang diinput kemudian di simpan di variabel "nominal"
                    System.out.print("Deskripsi: ");
                    String ket = sc.nextLine(); // input "teks" deskripsi dan simpan di variabel "ket" atau keterangan (tapi disini aku nyingkat aja)
                    mhs.get_keuangan().tambah_transaksi(nominal, ket);
                    break;
                case 2:
                    mhs.tampilSpec(); // buat nampilin spesifikasi kayak nama, saldo sekarang, dan daftar tugas yang belum selesai
                    break;
                case 3:
                    System.out.print("Masukkan Bulan (1-12): ");
                    int bln = sc.nextInt(); 
                    new Cetak_Laporan().proses_laporan(mhs.get_keuangan(), bln); //untuk menjalankan fungsi pembuatan laporan tanpa harus menyimpan objek pembuat laporan tersebut ke dalam variabel permanen
                    break;
                case 0:
                    berjalan = false;
                    System.out.println("Sampai jumpa, " + mhs.get_nama());
                    break;
            }
        }
    }
}

abstract class User { // abstark tapi harus ada pewarisan, jadi ga bisa asal dipanggil.. harus bikin pewarisan dulu
    protected String userId;
    protected String nama;
    protected String email;
    protected String password;
    protected String tipeUser;

    User(String id, String nama, String mail, String pass, String tipe) { // inisialisasi data
        this.userId = id;
        this.nama = nama;
        this.email = mail;
        this.password = pass;
        this.tipeUser = tipe;
    }

    public void login_user() {
        System.out.println("\n[Tungg!!] Selamat kepada " + nama + "!! kamu berhasil masuk ke sistem.");
    }

    public String get_nama() { return nama; }
}

abstract class Data_Pengguna extends User {
    protected String penggunaId;
    protected LocalDate tanggalDaftar;
    protected Boolean statusAktif;
    protected Keuangan_Mhs dompet; // komposisi

    Data_Pengguna(String id, String nama, String mail, String pass, String tipe, String pId, LocalDate tgl, Boolean stat) { // inisialisasi data
        super(id, nama, mail, pass, tipe);
        this.penggunaId = pId;
        this.tanggalDaftar = tgl;
        this.statusAktif = stat;
        this.dompet = new Keuangan_Mhs("K-" + pId, 0.0);
    }

    public Keuangan_Mhs get_keuangan() { return dompet; }
    public abstract void tampilSpec(); 
}

class Mahasiswa_Aktif extends Data_Pengguna {
    private List<Tugas_Mhs> list_tugas = new ArrayList<>();

    Mahasiswa_Aktif(String id, String nama, String mail, String pass, String tipe, String pId, LocalDate tgl, Boolean stat) {
        super(id, nama, mail, pass, tipe, pId, tgl, stat);
        list_tugas.add(new Tugas_Mhs("TG01", "Tugas Pemrograman", "OOP Java", LocalDate.now().plusDays(3), "Tinggi", "Proses", "Akademik"));
    } // konstruktur dari kelas induk.. jadi data ga usa diolah tinggal dilempar aja ke class induk

    @Override //pengulangan kode
    public void tampilSpec() {
        System.err.println("\n========== DASHBOARD MAHASISWA ==========");
        System.out.println("Nama Mahasiswa : " + get_nama());
        System.out.println("ID Pengguna    : " + this.penggunaId);
        System.out.println("Status Akun    : " + (this.statusAktif ? "Aktif" : "Non-Aktif"));
        System.out.println("Jumlah Tugas   : " + list_tugas.size());
        dompet.cek_saldo();
        System.err.println("=========================================");
    }
}

class Keuangan_Mhs { // class keuangan mahasiswa
    private String keuanganId;
    private double saldo_mhs;
    private List<Transaksi_Mhs> riwayat_transaksi = new ArrayList<>();

    Keuangan_Mhs(String id, double awal) { // inisialisasi daei class keuangan_mhs
        this.keuanganId = id;
        this.saldo_mhs = awal;
    }

    public void tambah_transaksi(double jml, String desc) { // fungsi tambahan buat nambahin transaksi
        this.saldo_mhs += jml;
        riwayat_transaksi.add(new Transaksi_Mhs("TR-" + System.currentTimeMillis(), LocalDate.now(), jml, desc));
        System.out.println("[SUKSES] Transaksi Rp" + jml + " sudah tercatat.");
    }

    public void cek_saldo() { // fungsi tambahana buat cek saldo
        System.out.println("Total Saldo : Rp" + String.format("%,.0f", saldo_mhs));
    }

    public List<Transaksi_Mhs> get_riwayat() { return riwayat_transaksi; } // Getter Method... Fungsinya adalah untuk memberikan akses kepada "pihak luar" agar bisa melihat atau mengambil data dari daftar riwayat transaksi.
}

class Transaksi_Mhs { // class buat fungsi tampilkan transaksi atau di bagian laporan bulanan
    String tId; LocalDate tgl; double total; String ket;
    Transaksi_Mhs(String id, LocalDate d, double t, String k) {
        this.tId = id; this.tgl = d; this.total = t; this.ket = k;
    }
}

class Tugas_Mhs { //class buat fungsi tugas mahasiswa
    String tId, judul, desc, prioritas, status, kategori;
    LocalDate deadline;
    Tugas_Mhs(String id, String j, String d, LocalDate dl, String p, String s, String k) {
        this.tId = id; this.judul = j; this.desc = d; this.deadline = dl;
        this.prioritas = p; this.status = s; this.kategori = k;
    }
}

interface Laporan_Keuangan {
    void proses_laporan(Keuangan_Mhs k, int bulan);
}

class Cetak_Laporan implements Laporan_Keuangan { //interface
    @Override
    public void proses_laporan(Keuangan_Mhs k, int bulan) {
        System.out.println("\n--- REKAP TRANSAKSI BULAN " + bulan + " ---");
        
        // Flag/Penanda untuk mengecek apakah ada data yang ditemukan
        boolean adaData = false; 
        double totalKeluarBulanIni = 0;

        for (Transaksi_Mhs t : k.get_riwayat()) {
            // Membandingkan bulan input dengan bulan pada data transaksi
            if (t.tgl.getMonthValue() == bulan) {
                System.out.println("- " + t.tgl + " | " + t.ket + " : Rp" + String.format("%,.0f", t.total));
                totalKeluarBulanIni += t.total;
                adaData = true; // Set true jika minimal ada satu transaksi
            }
        }

        // Jika setelah looping selesai tetap false, tampilkan pesan khusus
        if (!adaData) {
            System.out.println("  [!] Tidak ada catatan transaksi pada bulan ini.");
        } else {
            System.out.println("----------------------------------");
            System.out.printf("Total Penggunaan di Bulan %d: Rp%,.0f\n", bulan, totalKeluarBulanIni);
        }
        
        System.out.println("----------------------------------");
        k.cek_saldo(); // Menampilkan sisa uang secara keseluruhan
    }
}

interface Navigasi_Sistem { // fungsi interface untuk nontifikasi
    void kirim_notif(String pesan);
}

class Location_Service implements Navigasi_Sistem { //interface ke navigasi_sistem
    @Override
    public void kirim_notif(String pesan) {
        System.out.println("[ting.. tung..] " + pesan);
    }
}
