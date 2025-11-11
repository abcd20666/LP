import java.util.*;

public class SJF_NonPreemptive {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("*** Shortest Job First Scheduling (Non-Preemptive) ***");
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        int[] at = new int[n];   // Arrival Time
        int[] bt = new int[n];   // Burst Time
        int[] ct = new int[n];   // Completion Time
        int[] tat = new int[n];  // Turnaround Time
        int[] wt = new int[n];   // Waiting Time
        boolean[] done = new boolean[n];

        for (int i = 0; i < n; i++) {
            System.out.print("Enter Arrival Time for P" + (i + 1) + ": ");
            at[i] = sc.nextInt();
            System.out.print("Enter Burst Time for P" + (i + 1) + ": ");
            bt[i] = sc.nextInt();
            System.out.println();
        }

        int completed = 0, time = 0;
        double avgTAT = 0, avgWT = 0;

        while (completed < n) {
            int idx = -1, minBT = Integer.MAX_VALUE;

            // Find process with shortest burst among arrived ones
            for (int i = 0; i < n; i++) {
                if (!done[i] && at[i] <= time && bt[i] < minBT) {
                    minBT = bt[i];
                    idx = i;
                }
            }

            if (idx == -1) {  // No process has arrived yet
                time++;
            } else {
                time += bt[idx];
                ct[idx] = time;
                tat[idx] = ct[idx] - at[idx];
                wt[idx] = tat[idx] - bt[idx];
                done[idx] = true;
                completed++;
            }
        }

        System.out.println("\nProcess\tAT\tBT\tCT\tTAT\tWT");
        for (int i = 0; i < n; i++) {
            System.out.println("P" + (i + 1) + "\t" + at[i] + "\t" + bt[i] + "\t" +
                    ct[i] + "\t" + tat[i] + "\t" + wt[i]);
            avgTAT += tat[i];
            avgWT += wt[i];
        }

        System.out.printf("\nAverage Turnaround Time: %.2f", avgTAT / n);
        System.out.printf("\nAverage Waiting Time: %.2f\n", avgWT / n);

        sc.close();
    }
}


/**** Shortest Job First Scheduling (Non-Preemptive) ***
Enter number of processes: 4
Enter Arrival Time for P1: 1
Enter Burst Time for P1: 3

Enter Arrival Time for P2: 1
Enter Burst Time for P2: 2

Enter Arrival Time for P3: 2
Enter Burst Time for P3: 4

Enter Arrival Time for P4: 4
Enter Burst Time for P4: 4


Process AT      BT      CT      TAT     WT
P1      1       3       6       5       2
P2      1       2       3       2       0
P3      2       4       10      8       4
P4      4       4       14      10      6

Average Turnaround Time: 6.25
Average Waiting Time: 3.00 */

/*import java.util.*;

public class SJF_NonPreemptive {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("*** Shortest Job First Scheduling (Non-Preemptive) ***");
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        int[] at = new int[n];   // Arrival Time
        int[] bt = new int[n];   // Burst Time
        int[] ct = new int[n];   // Completion Time
        int[] tat = new int[n];  // Turnaround Time
        int[] wt = new int[n];   // Waiting Time
        boolean[] done = new boolean[n];

        // For Gantt Chart
        ArrayList<String> ganttProcess = new ArrayList<>();
        ArrayList<Integer> ganttTime = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            System.out.print("Enter Arrival Time for P" + (i + 1) + ": ");
            at[i] = sc.nextInt();
            System.out.print("Enter Burst Time for P" + (i + 1) + ": ");
            bt[i] = sc.nextInt();
            System.out.println();
        }

        int completed = 0, time = 0;
        double avgTAT = 0, avgWT = 0;

        // Start Gantt chart with initial time (if first process arrives later)
        int firstArrival = Arrays.stream(at).min().getAsInt();
        time = firstArrival;
        ganttTime.add(time);

        while (completed < n) {
            int idx = -1, minBT = Integer.MAX_VALUE;

            // Find process with shortest burst among arrived ones
            for (int i = 0; i < n; i++) {
                if (!done[i] && at[i] <= time && bt[i] < minBT) {
                    minBT = bt[i];
                    idx = i;
                }
            }

            if (idx == -1) {  // No process has arrived yet
                time++;
            } else {
                ganttProcess.add("P" + (idx + 1));
                time += bt[idx];
                ganttTime.add(time);

                ct[idx] = time;
                tat[idx] = ct[idx] - at[idx];
                wt[idx] = tat[idx] - bt[idx];
                done[idx] = true;
                completed++;
            }
        }

        System.out.println("\nProcess\tAT\tBT\tCT\tTAT\tWT");
        System.out.println("-------------------------------------");
        for (int i = 0; i < n; i++) {
            System.out.println("P" + (i + 1) + "\t" + at[i] + "\t" + bt[i] + "\t" +
                    ct[i] + "\t" + tat[i] + "\t" + wt[i]);
            avgTAT += tat[i];
            avgWT += wt[i];
        }

        System.out.printf("\nAverage Turnaround Time: %.2f", avgTAT / n);
        System.out.printf("\nAverage Waiting Time: %.2f\n", avgWT / n);

        // Display Gantt Chart
        System.out.println("\n\nGantt Chart:");
        System.out.print(" ");
        for (String p : ganttProcess)
            System.out.print("----");
        System.out.println("-");
        System.out.print("|");
        for (String p : ganttProcess)
            System.out.print(" " + p + " |");
        System.out.println();
        System.out.print(" ");
        for (String p : ganttProcess)
            System.out.print("----");
        System.out.println("-");
        for (int t : ganttTime)
            System.out.printf("%-4d", t);
        System.out.println();
    }
}
 */