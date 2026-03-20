import java.time.LocalDate; // untuk nampilin tanggal sekaranr
import java.util.ArrayList; // agar bisa dipanggil didalam fungsi-fungsi lain
import java.util.List;
import java.util.Scanner; // agar bisa ngeprint

class User { // class tambahan
    private String userId;
    private String nama;
    private String email;
    private String password;
    private String tipeUser;

    public User(String userId, String nama, String email, String password, String tipeUser){
        this.userId = userId; this.nama = nama; this.email = email;
        this.password = password; this.tipeUser = tipeUser;
    }

    public void login(){ System.out.println("\n==>>> " + nama + " berhasil login."); }
    public String getNama(){ return nama; }
} 

class Pengguna extends User { // Inheritance
    private String penggunaId;
    private LocalDate tanggalDaftar; 
    private Boolean statusAktif;
    private Keuangan keuangan; // Composition
    private List<Tugas> daftarTugas = new ArrayList<>();
    private List<Target> daftarTarget = new ArrayList<>();

    public Pengguna(String userId, String nama, String email, String password, String tipeUser, String penggunaId, LocalDate tanggalDaftar, Boolean statusAktif){
        super(userId, nama, email, password, tipeUser);
        this.penggunaId = penggunaId;
        this.tanggalDaftar = tanggalDaftar;
        this.statusAktif = statusAktif;
        this.keuangan = new Keuangan("K-" + penggunaId, 0.0);
    }
    
    public void viewDashboard() {
        System.out.println("\n=============================================");
        System.out.println("          DASHBOARD MANAJEMEN MAHASISWA      ");
        System.out.println("=============================================");
        System.out.println("Halo, " + super.getNama() + "!");
        System.out.println("Tugas Kuliah Aktif : " + daftarTugas.size() + " tugas.");
        
        System.out.println("\n--- Rincian Tugas ---");
        if(daftarTugas.isEmpty()) {
            System.out.println("(Belum ada tugas yang dicatat)");
        } else {
            for(int i=0; i<daftarTugas.size(); i++) {
                System.out.println((i+1) + ". " + daftarTugas.get(i).getJudul());
            }
        }
        
        System.out.println("\n--- Status Keuangan ---");
        keuangan.hitungSaldo();
        System.out.println("=============================================\n");
    }

    public void manageTugas(Tugas tugasBaru) {
        daftarTugas.add(tugasBaru);
        System.out.println(">>> [BERHASIL] Tugas '" + tugasBaru.getJudul() + "' ditambahkan!");
    }

    public Keuangan manageKeuangan() { return this.keuangan; }
} 

class Keuangan {
    private String keuanganId;
    private double saldo; // Encapsulation
    private double totalPendapatan;
    private double totalPengeluaran;
    private List<Transaksi> daftarTransaksi = new ArrayList<>();

    public Keuangan(String keuanganId, double saldoAwal) {
        this.keuanganId = keuanganId;
        this.saldo = saldoAwal;
    }

    public void tambahPendapatan(Transaksi t) {
        saldo += t.getJumlah();
        totalPendapatan += t.getJumlah();
        daftarTransaksi.add(t);
        System.out.println(">>> [BERHASIL] Pemasukan Rp" + String.format("%,.0f", t.getJumlah()) + " dicatat!");
    }

    public void tambahPengeluaran(Transaksi t) {
        if (saldo >= t.getJumlah()) {
            saldo -= t.getJumlah();
            totalPengeluaran += t.getJumlah();
            daftarTransaksi.add(t);
            System.out.println(">>> [BERHASIL] Pengeluaran Rp" + String.format("%,.0f", t.getJumlah()) + " dicatat!");
        } else {
            System.out.println(">>> [GAGAL] Saldo Anda tidak cukup! Sisa saldo: Rp" + String.format("%,.0f", saldo));
        }
    }

    public void hitungSaldo() {
        System.out.println("Sisa Saldo Saat Ini: Rp" + String.format("%,.0f", saldo));
    }

    public List<Transaksi> getDaftarTransaksi() {
        return daftarTransaksi; 
    }
} 

class Transaksi {
    private String transaksiId;
    private LocalDate tanggal;
    private double jumlah;
    private Kategori kategori; 
    private String deskripsi;
    private String tipe; 

    public Transaksi(String transaksiId, LocalDate tanggal, double jumlah, Kategori kategori, String deskripsi, String tipe) {
        this.transaksiId = transaksiId; this.tanggal = tanggal; this.jumlah = jumlah;
        this.kategori = kategori; this.deskripsi = deskripsi; this.tipe = tipe;
    }
    
    // kode kebawah ini Getter
    public double getJumlah() { 
        return jumlah;
    }
    public String getDeskripsi() {
        return deskripsi;
    }
    public LocalDate getTanggal() {
        return tanggal;
    }
    public String getTipe() {
        return tipe;
    }
    public Kategori getKategori() {
        return kategori;
    }
    // sampe sini
}

class Kategori {
    private String kategoriId;
    private String nama;
    private String warna;
    private String deskripsi;

    public Kategori(String kategoriId, String nama, String warna, String deskripsi) {
        this.kategoriId = kategoriId; this.nama = nama; this.warna = warna; this.deskripsi = deskripsi;
    }
    public String getNama() { return nama; }
}

class Laporan {
    private String laporanId;
    private String tipeData;
    private String periode;
    private LocalDate tanggalBuat;

    public Laporan(String laporanId, String tipeData, String periode) {
        this.laporanId = laporanId; this.tipeData = tipeData;
        this.periode = periode; this.tanggalBuat = LocalDate.now();
    }

    public void buatLaporanKeuangan(Keuangan keuangan, int bulan, int tahun) {
        System.out.println("\n---------------------------------------------");
        System.out.println("LAPORAN PENGELUARAN BULANAN (" + periode + ")");
        System.out.println("---------------------------------------------");
        
        double totalPengeluaranBulanIni = 0.0;
        boolean adaTransaksi = false;

        for (Transaksi t : keuangan.getDaftarTransaksi()) {
            if (t.getTanggal().getMonthValue() == bulan && 
                t.getTanggal().getYear() == tahun && 
                t.getTipe().equalsIgnoreCase("Pengeluaran")) {
                
                System.out.printf("- %s | [%-15s] Rp%,.0f (%s)\n", 
                                  t.getTanggal(), t.getKategori().getNama(), 
                                  t.getJumlah(), t.getDeskripsi());
                totalPengeluaranBulanIni += t.getJumlah();
                adaTransaksi = true;
            }
        }

        if (!adaTransaksi) { System.out.println("  (Tidak ada pengeluaran tercatat)"); }
        System.out.println("---------------------------------------------");
        System.out.printf("TOTAL PENGELUARAN : Rp%,.0f\n", totalPengeluaranBulanIni);
        System.out.println("---------------------------------------------\n");
    }
}

class Tugas {
    private String tugasId, judul, deskripsi, prioritas, status, kategoriTugas;
    private LocalDate tanggalDeadline;

    public Tugas(String id, String jdl, String desk, LocalDate dl, String prio, String stat, String kat) {
        this.tugasId = id; this.judul = jdl; this.deskripsi = desk; 
        this.tanggalDeadline = dl; this.prioritas = prio; 
        this.status = stat; this.kategoriTugas = kat;
    }
    public String getJudul() { return judul; }
}

class Target {
    private String targetId, deskripsi, kategori;
    private double jumlahTarget;

    public Target(String targetId, String deskripsi, double jumlahTarget, String kategori) {
        this.targetId = targetId; 
        this.deskripsi = deskripsi; 
        this.jumlahTarget = jumlahTarget; // <-- TYPO SUDAH DIPERBAIKI DI SINI
        this.kategori = kategori;
    }
    public String getDeskripsi() { return deskripsi; }
}

// fungsi main
public class App {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("=== SELAMAT DATANG DI SISTEM MANAJEMEN MAHASISWA ===");
        
        // 1. INPUT DATA DIRI 
        System.out.print("Masukkan Nama Anda : ");
        String namaUser = input.nextLine();
        System.out.print("Masukkan NIM Anda  : ");
        String nimUser = input.nextLine();

        // Membuat objek pengguna
        Pengguna mahasiswa = new Pengguna(nimUser, namaUser, nimUser+"@student.its.ac.id", 
                                      "pass123", "Mahasiswa", "P-001", LocalDate.now(), true);
        mahasiswa.login();

        Keuangan dompetKu = mahasiswa.manageKeuangan();
        
        // Kategori standar 
        Kategori katUmum = new Kategori("KT-00", "Umum", "Putih", "Kategori Bebas");

        // 2. LOOPING MENU INTERAKTIF
        boolean isRunning = true;
        
        while(isRunning) {
            System.out.println("\n============= MENU UTAMA =============");
            System.out.println("1. Tambah Pemasukan (Jatah Ortu/Gajian)");
            System.out.println("2. Catat Pengeluaran (Makan/Kos/dll)");
            System.out.println("3. Tambah Tugas Kuliah");
            System.out.println("4. Lihat Laporan Keuangan Bulan Ini");
            System.out.println("5. Lihat Dashboard Status");
            System.out.println("0. Keluar Aplikasi");
            System.out.print("Pilih menu (0-5): ");
        
            
            // Validasi input agar tidak error kalau user iseng masukin huruf
            if (!input.hasNextInt()) {
                System.out.println("Harap masukkan angka yang valid!");
                input.next(); 
                continue;
            }

            int pilihan = input.nextInt();
            input.nextLine(); // membersihkan "Enter" dari input angka 

            System.out.println("--------------------------------------");

            // 3. LOGIKA UNTUK SETIAP MENU
            switch(pilihan) {
                case 1:
                    System.out.print("Masukkan jumlah uang (contoh: 1500000): Rp ");
                    double masuk = input.nextDouble();
                    input.nextLine(); 
                    System.out.print("Deskripsi (contoh: Uang saku bulan ini): ");
                    String deskMasuk = input.nextLine();
                    
                    dompetKu.tambahPendapatan(new Transaksi("TRX-IN", LocalDate.now(), masuk, katUmum, deskMasuk, "Pendapatan"));
                    break;

                case 2:
                    System.out.print("Masukkan jumlah pengeluaran: Rp ");
                    double keluar = input.nextDouble();
                    input.nextLine(); 
                    System.out.print("Deskripsi (contoh: Beli Nasi Padang): ");
                    String deskKeluar = input.nextLine();
                    
                    dompetKu.tambahPengeluaran(new Transaksi("TRX-OUT", LocalDate.now(), keluar, katUmum, deskKeluar, "Pengeluaran"));
                    break;

                case 3:
                    System.out.print("Judul Tugas (contoh: Praktikum OS): ");
                    String judulTugas = input.nextLine();
                    System.out.print("Deskripsi Tugas: ");
                    String deskTugas = input.nextLine();
                    
                    mahasiswa.manageTugas(new Tugas("TG-01", judulTugas, deskTugas, LocalDate.now().plusDays(2), "Tinggi", "Belum", "Akademik"));
                    break;

                case 4:
                    int bulanIni = LocalDate.now().getMonthValue();
                    int tahunIni = LocalDate.now().getYear();
                    Laporan lapBulanan = new Laporan("LAP-01", "Keuangan", "Bulan " + bulanIni + " / " + tahunIni);
                    lapBulanan.buatLaporanKeuangan(dompetKu, bulanIni, tahunIni);
                    break;

                case 5:
                    mahasiswa.viewDashboard();
                    break;

                case 0:
                    System.out.println("Menyimpan data... Sampai jumpa, " + mahasiswa.getNama() + "!");
                    isRunning = false; 
                    break;

                default:
                    System.out.println("Pilihan tidak valid! Silakan pilih angka 0-5.");
            }
        }
        
        input.close(); 
    }
}