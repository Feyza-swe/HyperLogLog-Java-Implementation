
public class HyperLogLog {
    private final int p; // Hassasiyet (bit sayısı)
    private final int m; // Kova sayısı (2^p)
    private final byte[] registers;
    private final double alpha;

    public HyperLogLog(int p) {
        if (p < 4 || p > 16) throw new IllegalArgumentException("p 4 ile 16 arasında olmalı");
        this.p = p;
        this.m = 1 << p;
        this.registers = new byte[m];
        this.alpha = getAlpha(m);
    }


    private int hash(String val) {
        // Burada basit bir 32-bit hash simülasyonu yapıyoruz
        int h = 0xdeadbeef;
        for (char c : val.toCharArray()) {
            h ^= c;
            h *= 0x5bd1e995;
            h ^= h >>> 15;
        }
        return h;
    }

    public void add(String element) {
        int x = hash(element);
        
        // İlk p biti kova indeksi için kullan
        int j = x >>> (32 - p);
        
        // Kalan bitlerdeki ardışık sıfırları say
        // Java'nın hazır metodu: Integer.numberOfLeadingZeros
        // Sadece kalan bitlere bakmak için maskeleme yapıyoruz
        int w = x << p;
        int rho = Integer.numberOfLeadingZeros(w) + 1;
        if (w == 0) rho = 32 - p + 1; // Hepsi sıfırsa sınır değeri

        // Kovadaki maksimum değeri güncelle
        if (rho > registers[j]) {
            registers[j] = (byte) rho;
        }
    }

    public long estimate() {
        double sum = 0;
        for (int r : registers) {
            sum += Math.pow(2, -r);
        }

        double E = alpha * m * m * (1.0 / sum);

        // Küçük veri düzeltmesi (Linear Counting)
        if (E <= 2.5 * m) {
            int V = 0;
            for (int r : registers) if (r == 0) V++;
            if (V > 0) E = m * Math.log((double) m / V);
        }
        // Büyük veri düzeltmesi
        else if (E > (1.0 / 30.0) * Math.pow(2, 32)) {
            E = -Math.pow(2, 32) * Math.log(1.0 - E / Math.pow(2, 32));
        }

        return Math.round(E);
    }

    private double getAlpha(int m) {
        if (m == 16) return 0.673;
        if (m == 32) return 0.697;
        if (m == 64) return 0.709;
        return 0.7213 / (1 + 1.079 / m);
    }

    // Birleştirme (Merge) işlemi
    public void merge(HyperLogLog other) {
        if (this.p != other.p) throw new IllegalArgumentException("Hassasiyetler aynı olmalı");
        for (int i = 0; i < m; i++) {
            this.registers[i] = (byte) Math.max(this.registers[i], other.registers[i]);
        }
    }

    public static void main(String[] args) {
        HyperLogLog hll = new HyperLogLog(10); // 1024 kova
        
        // 10.000 benzersiz öğe ekleyelim
        for (int i = 0; i < 10000; i++) {
            hll.add("user_" + i);
        }

        System.out.println("Gerçek Sayı: 10000");
        System.out.println("HLL Tahmini: " + hll.estimate());
    }
}