import java.util.Scanner;

public class PenjumlahanParalel {
    // Shared variable untuk menyimpan total akhir
    private static long totalSum = 0;

    // Method synchronized untuk menjumlahkan hasil parsial ke total akhir dengan aman
    private static synchronized void addTotalSum(long partialResult) {
        totalSum += partialResult;
    }

    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Masukkan Jumlah Thread: ");
        int numThreads = scanner.nextInt();

        System.out.print("Masukkan Angka Akhir: ");
        long maxNumber = scanner.nextLong();
        
        scanner.close();

        // Array untuk menyimpan object Thread
        Thread[] threads = new Thread[numThreads];
        
        // Menentukan ukuran range per thread
        long chunkSize = maxNumber / numThreads;
        long remainder = maxNumber % numThreads;

        long currentStart = 1;

        System.out.println("\n--- Proses Penjumlahan ---");

        for (int i = 0; i < numThreads; i++) {
            final int threadId = i + 1;
            final long start = currentStart;
            
            // Thread terakhir akan menangani sisa pembagian (remainder)
            final long end = (i == numThreads - 1) ? (currentStart + chunkSize + remainder - 1) : (currentStart + chunkSize - 1);
            
            currentStart = end + 1;

            threads[i] = new Thread(() -> {
                long partialSum = 0;
                // Melakukan iterasi dari start ke end
                for (long j = start; j <= end; j++) {
                    partialSum += j;
                }
                
                System.out.println("Thread " + threadId + ": Menjumlahkan " + start + " - " + end + " | Hasil Parsial: " + partialSum);
                
                // Menambahkan hasil parsial ke total akhir secara sinkron (Thread Safety)
                addTotalSum(partialSum);
            });

            threads[i].start();
        }

        // Memastikan Main Thread menunggu semua thread pekerja selesai
        for (Thread t : threads) {
            t.join();
        }

        System.out.println("\n--- HASIL AKHIR ---");
        System.out.println("Total Penjumlahan dari 1 sampai " + maxNumber + " adalah: " + totalSum);
    }
}