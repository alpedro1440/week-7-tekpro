class Account {
    int balance = 150;
}

public class TransferFulus {
    public static void main(String[] args) throws InterruptedException {
        Account acc1 = new Account();
        Account acc2 = new Account();

        // Thread 1: Menjumlahkan/ transfer fulus dari acc1 ke acc2
        Thread t1 = new Thread(() -> {
            // SOLUSI: Mengunci acc1 terlebih dahulu
            synchronized (acc1) { // Mengunci object acc1 agar thread lain tidak bisa memodifikasinya
                System.out.println("Thread 1: Mengunci acc1, bersiap memindahkan saldo ke acc2...");
                
                // Exception (InterruptedException) diperlukan karena method sleep()
                // bisa diinterupsi oleh thread lain saat sedang tertidur/jeda.
                try { Thread.sleep(100); } catch (Exception e) {} 
                
                synchronized (acc2) { // Mengunci object acc2 untuk memastikan keamanan saat penambahan
                    System.out.println("Thread 1: Mengunci acc2, melakukan transfer...");
                    acc2.balance += acc1.balance;
                }
            }
        });

        // Thread 2: Menjumlahkan/ transfer fulus dari acc2 ke acc1
        Thread t2 = new Thread(() -> {
            // SOLUSI: Mengubah urutan lock agar SAMA dengan Thread 1 untuk mencegah Deadlock
            synchronized (acc1) { // Tetap mengunci acc1 terlebih dahulu
                System.out.println("Thread 2: Mengunci acc1, bersiap menerima saldo dari acc2...");
                try { Thread.sleep(100); } catch (Exception e) {}
                
                synchronized (acc2) { // Kemudian baru mengunci acc2
                    System.out.println("Thread 2: Mengunci acc2, melakukan perhitungan...");
                    acc1.balance += acc2.balance;
                }
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("--- HASIL AKHIR ---");
        System.out.println("Saldo Akhir acc1: " + acc1.balance);
        System.out.println("Saldo Akhir acc2: " + acc2.balance);
    }
}