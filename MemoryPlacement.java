import java.util.*;

public class MemoryPlacement {

    // ---------------- FIRST FIT ----------------
    static void firstFit(int blockSize[], int m, int processSize[], int n) {
        int allocation[] = new int[n];
        Arrays.fill(allocation, -1);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (blockSize[j] >= processSize[i]) {
                    allocation[i] = j;
                    blockSize[j] -= processSize[i];
                    break;
                }
            }
        }

        display(allocation, processSize, n);
    }

    // ---------------- BEST FIT ----------------
    static void bestFit(int blockSize[], int m, int processSize[], int n) {
        int allocation[] = new int[n];
        Arrays.fill(allocation, -1);

        for (int i = 0; i < n; i++) {
            int bestIdx = -1;
            for (int j = 0; j < m; j++) {
                if (blockSize[j] >= processSize[i]) {
                    if (bestIdx == -1 || blockSize[j] < blockSize[bestIdx])
                        bestIdx = j;
                }
            }

            if (bestIdx != -1) {
                allocation[i] = bestIdx;
                blockSize[bestIdx] -= processSize[i];
            }
        }

        display(allocation, processSize, n);
    }

    // ---------------- WORST FIT ----------------
    static void worstFit(int blockSize[], int m, int processSize[], int n) {
        int allocation[] = new int[n];
        Arrays.fill(allocation, -1);

        for (int i = 0; i < n; i++) {
            int worstIdx = -1;
            for (int j = 0; j < m; j++) {
                if (blockSize[j] >= processSize[i]) {
                    if (worstIdx == -1 || blockSize[j] > blockSize[worstIdx])
                        worstIdx = j;
                }
            }

            if (worstIdx != -1) {
                allocation[i] = worstIdx;
                blockSize[worstIdx] -= processSize[i];
            }
        }

        display(allocation, processSize, n);
    }

    // ---------------- NEXT FIT ----------------
    static void nextFit(int blockSize[], int m, int processSize[], int n) {
        int allocation[] = new int[n];
        Arrays.fill(allocation, -1);

        int j = 0; // Pointer for current block
        for (int i = 0; i < n; i++) {
            int count = 0;
            while (count < m) {
                if (blockSize[j] >= processSize[i]) {
                    allocation[i] = j;
                    blockSize[j] -= processSize[i];
                    break;
                }
                j = (j + 1) % m;
                count++;
            }
        }

        display(allocation, processSize, n);
    }

    // ---------------- DISPLAY FUNCTION ----------------
    static void display(int allocation[], int processSize[], int n) {
        System.out.println("\nProcess No.\tProcess Size\tBlock No.");
        for (int i = 0; i < n; i++) {
            System.out.print((i + 1) + "\t\t" + processSize[i] + "\t\t");
            if (allocation[i] != -1)
                System.out.println(allocation[i] + 1);
            else
                System.out.println("Not Allocated");
        }
    }

    // ---------------- MAIN METHOD ----------------
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("\nEnter number of memory blocks: ");
        int m = sc.nextInt();
        int blockSize[] = new int[m];
        System.out.println("Enter block sizes:");
        for (int i = 0; i < m; i++)
            blockSize[i] = sc.nextInt();

        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();
        int processSize[] = new int[n];
        System.out.println("Enter process sizes:");
        for (int i = 0; i < n; i++)
            processSize[i] = sc.nextInt();

        while (true) {
            System.out.println("\nChoose Memory Allocation Strategy:");
            System.out.println("1. First Fit");
            System.out.println("2. Best Fit");
            System.out.println("3. Worst Fit");
            System.out.println("4. Next Fit");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            int blockCopy[] = Arrays.copyOf(blockSize, m);

            switch (choice) {
                case 1:
                    System.out.println("\n--- FIRST FIT ALLOCATION ---");
                    firstFit(blockCopy, m, processSize, n);
                    break;
                case 2:
                    System.out.println("\n--- BEST FIT ALLOCATION ---");
                    bestFit(blockCopy, m, processSize, n);
                    break;
                case 3:
                    System.out.println("\n--- WORST FIT ALLOCATION ---");
                    worstFit(blockCopy, m, processSize, n);
                    break;
                case 4:
                    System.out.println("\n--- NEXT FIT ALLOCATION ---");
                    nextFit(blockCopy, m, processSize, n);
                    break;
                case 5:
                    System.out.println("Program terminated.");
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid Choice!");
            }
        }
    }
}

/* 

Enter number of memory blocks: 5
Enter block sizes:
100 500 200 300 600
Enter number of processes: 4
Enter process sizes:
212 417 112 426

Choose Memory Allocation Strategy:
1. First Fit
2. Best Fit
3. Worst Fit
4. Next Fit
5. Exit
Enter your choice: 1

--- FIRST FIT ALLOCATION ---

Process No.     Process Size    Block No.
1               212             2
2               417             5
3               112             2
4               426             Not Allocated

Choose Memory Allocation Strategy:
1. First Fit
2. Best Fit
3. Worst Fit
4. Next Fit
5. Exit
Enter your choice: 2

--- BEST FIT ALLOCATION ---

Process No.     Process Size    Block No.
1               212             4
2               417             2
3               112             3
4               426             5

Choose Memory Allocation Strategy:
1. First Fit
2. Best Fit
3. Worst Fit
4. Next Fit
5. Exit
Enter your choice: 3

--- WORST FIT ALLOCATION ---

Process No.     Process Size    Block No.
1               212             5
2               417             2
3               112             5
4               426             Not Allocated

Choose Memory Allocation Strategy:
1. First Fit
2. Best Fit
3. Worst Fit
4. Next Fit
5. Exit
Enter your choice: 4

--- NEXT FIT ALLOCATION ---

Process No.     Process Size    Block No.
1               212             2
2               417             5
3               112             5
4               426             Not Allocated

Choose Memory Allocation Strategy:
1. First Fit
2. Best Fit
3. Worst Fit
4. Next Fit
5. Exit
Enter your choice: 5
Program terminated.
*/