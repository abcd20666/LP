import java.util.Scanner;

public class FCFS {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("*** First Come First Serve (Non-Preemptive) Scheduling ***");
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        int[] pid = new int[n];   // Process IDs
        int[] at = new int[n];    // Arrival Time
        int[] bt = new int[n];    // Burst Time
        int[] ct = new int[n];    // Completion Time
        int[] tat = new int[n];   // Turnaround Time
        int[] wt = new int[n];    // Waiting Time

        // Input process data
        for (int i = 0; i < n; i++) {
            pid[i] = i + 1;
            System.out.print("Enter Arrival Time for P" + pid[i] + ": ");
            at[i] = sc.nextInt();
            System.out.print("Enter Burst Time for P" + pid[i] + ": ");
            bt[i] = sc.nextInt();
            System.out.println();
        }

        // Sort processes by Arrival Time
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (at[i] > at[j]) {
                    int temp;

                    temp = at[i]; at[i] = at[j]; at[j] = temp;
                    temp = bt[i]; bt[i] = bt[j]; bt[j] = temp;
                    temp = pid[i]; pid[i] = pid[j]; pid[j] = temp;
                }
            }
        }

        // Calculate Completion Time (CT)
        ct[0] = at[0] + bt[0];
        for (int i = 1; i < n; i++) {
            if (at[i] > ct[i - 1])
                ct[i] = at[i] + bt[i];     // CPU is idle
            else
                ct[i] = ct[i - 1] + bt[i]; // CPU executes immediately
        }

        // Calculate Turnaround Time (TAT) and Waiting Time (WT)
        float totalTAT = 0, totalWT = 0;
        for (int i = 0; i < n; i++) {
            tat[i] = ct[i] - at[i];
            wt[i] = tat[i] - bt[i];
            totalTAT += tat[i];
            totalWT += wt[i];
        }

        // Display Table
        System.out.println("\nProcess\tAT\tBT\tCT\tTAT\tWT");
        System.out.println("-------------------------------------");
        for (int i = 0; i < n; i++) {
            System.out.println("P" + pid[i] + "\t" + at[i] + "\t" + bt[i] + "\t" +
                               ct[i] + "\t" + tat[i] + "\t" + wt[i]);
        }

        // Display Averages
        System.out.printf("\nAverage Turnaround Time = %.2f", (totalTAT / n));
        System.out.printf("\nAverage Waiting Time = %.2f\n", (totalWT / n));

        sc.close();
    }
}
/**** First Come First Serve (Non-Preemptive) Scheduling ***
Enter number of processes: 4
Enter Arrival Time for P1: 0
Enter Burst Time for P1: 2

Enter Arrival Time for P2: 1
Enter Burst Time for P2: 2

Enter Arrival Time for P3: 5
Enter Burst Time for P3: 3

Enter Arrival Time for P4: 6
Enter Burst Time for P4: 4


Process AT      BT      CT      TAT     WT  
-------------------------------------       
P1      0       2       2       2       0
P2      1       2       4       3       1
P3      5       3       8       3       0
P4      6       4       12      6       2

Average Turnaround Time = 3.50
Average Waiting Time = 0.75 */


/*import java.util.Scanner;

public class FCFS {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("*** First Come First Serve (Non-Preemptive) Scheduling ***");
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        int[] pid = new int[n];   // Process IDs
        int[] at = new int[n];    // Arrival Time
        int[] bt = new int[n];    // Burst Time
        int[] ct = new int[n];    // Completion Time
        int[] tat = new int[n];   // Turnaround Time
        int[] wt = new int[n];    // Waiting Time
        int[] start = new int[n]; // Start Time (for Gantt chart)

        // Input process data
        for (int i = 0; i < n; i++) {
            pid[i] = i + 1;
            System.out.print("Enter Arrival Time for P" + pid[i] + ": ");
            at[i] = sc.nextInt();
            System.out.print("Enter Burst Time for P" + pid[i] + ": ");
            bt[i] = sc.nextInt();
            System.out.println();
        }

        // Sort processes by Arrival Time
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (at[i] > at[j]) {
                    int temp;

                    temp = at[i]; at[i] = at[j]; at[j] = temp;
                    temp = bt[i]; bt[i] = bt[j]; bt[j] = temp;
                    temp = pid[i]; pid[i] = pid[j]; pid[j] = temp;
                }
            }
        }

        // Calculate Completion, Start, Turnaround, and Waiting times
        ct[0] = at[0] + bt[0];
        start[0] = at[0];
        for (int i = 1; i < n; i++) {
            if (at[i] > ct[i - 1]) { // CPU idle
                start[i] = at[i];
                ct[i] = at[i] + bt[i];
            } else {                 // CPU continues
                start[i] = ct[i - 1];
                ct[i] = ct[i - 1] + bt[i];
            }
        }

        float totalTAT = 0, totalWT = 0;
        for (int i = 0; i < n; i++) {
            tat[i] = ct[i] - at[i];
            wt[i] = tat[i] - bt[i];
            totalTAT += tat[i];
            totalWT += wt[i];
        }

        // Display Table
        System.out.println("\nProcess\tAT\tBT\tCT\tTAT\tWT");
        System.out.println("--------------------------------------");
        for (int i = 0; i < n; i++) {
            System.out.println("P" + pid[i] + "\t" + at[i] + "\t" + bt[i] + "\t" +
                               ct[i] + "\t" + tat[i] + "\t" + wt[i]);
        }

        // Display Averages
        System.out.printf("\nAverage Turnaround Time = %.2f", (totalTAT / n));
        System.out.printf("\nAverage Waiting Time = %.2f\n", (totalWT / n));

        // Display Gantt Chart
        System.out.println("\n\nGANTT CHART:");
        System.out.print(" ");

        // Upper bar
        for (int i = 0; i < n; i++) {
            System.out.print("-------");
        }
        System.out.println();

        // Process names
        System.out.print("|");
        for (int i = 0; i < n; i++) {
            System.out.printf("  P%d  |", pid[i]);
        }
        System.out.println();

        // Lower bar
        System.out.print(" ");
        for (int i = 0; i < n; i++) {
            System.out.print("-------");
        }
        System.out.println();

        // Time values
        System.out.print(start[0]);
        for (int i = 0; i < n; i++) {
            System.out.printf("\t%d", ct[i]);
        }
        System.out.println();

        sc.close();
    }
}
 */