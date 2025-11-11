import java.util.Scanner;

public class PSNPHigh{

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("*** Priority Scheduling (Non-Preemptive, Higher Number = Higher Priority) ***");
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        int[] pid = new int[n];
        int[] at = new int[n];   // Arrival time
        int[] bt = new int[n];   // Burst time
        int[] pr = new int[n];   // Priority
        int[] ct = new int[n];   // Completion time
        int[] tat = new int[n];  // Turnaround time
        int[] wt = new int[n];   // Waiting time

        for (int i = 0; i < n; i++) {
            pid[i] = i + 1;
            System.out.print("Enter Arrival Time for P" + pid[i] + ": ");
            at[i] = sc.nextInt();
            System.out.print("Enter Burst Time for P" + pid[i] + ": ");
            bt[i] = sc.nextInt();
            System.out.print("Enter Priority for P" + pid[i] + ": ");
            pr[i] = sc.nextInt();
        }

        boolean[] done = new boolean[n];
        int completed = 0, time = 0;
        double totalTAT = 0, totalWT = 0;

        while (completed < n) {
            int idx = -1;
            int maxPriority = Integer.MIN_VALUE;

            // Find process with highest priority (largest number) that has arrived
            for (int i = 0; i < n; i++) {
                if (!done[i] && at[i] <= time && pr[i] > maxPriority) {
                    maxPriority = pr[i];
                    idx = i;
                }
            }

            if (idx != -1) {
                time += bt[idx];
                ct[idx] = time;
                tat[idx] = ct[idx] - at[idx];
                wt[idx] = tat[idx] - bt[idx];
                done[idx] = true;
                completed++;
            } else {
                time++;
            }
        }

        System.out.println("\nPID\tAT\tBT\tPR\tCT\tTAT\tWT");
        for (int i = 0; i < n; i++) {
            System.out.println(pid[i] + "\t" + at[i] + "\t" + bt[i] + "\t" + pr[i] + "\t" +
                    ct[i] + "\t" + tat[i] + "\t" + wt[i]);
            totalTAT += tat[i];
            totalWT += wt[i];
        }

        System.out.printf("\nAverage Turnaround Time: %.2f", totalTAT / n);
        System.out.printf("\nAverage Waiting Time: %.2f\n", totalWT / n);

        sc.close();
    }
}
/* 
*** Priority Scheduling (Non-Preemptive, Higher Number = Higher Priority) ***
Enter number of processes: 4
Enter Arrival Time for P1: 0
Enter Burst Time for P1: 5
Enter Priority for P1: 1
Enter Arrival Time for P2: 1
Enter Burst Time for P2: 4
Enter Priority for P2: 2
Enter Arrival Time for P3: 2
Enter Burst Time for P3: 2
Enter Priority for P3: 3
Enter Arrival Time for P4: 4
Enter Burst Time for P4: 1
Enter Priority for P4: 4

PID     AT      BT      PR      CT      TAT     WT
1       0       5       1       5       5       0
2       1       4       2       12      11      7
3       2       2       3       8       6       4
4       4       1       4       6       2       1

Average Turnaround Time: 6.00
Average Waiting Time: 3.00
*/


/*import java.util.Scanner;

public class PSNPHigh {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("*** Priority Scheduling (Non-Preemptive, Higher Number = Higher Priority) ***");
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        int[] pid = new int[n];
        int[] at = new int[n];   // Arrival time
        int[] bt = new int[n];   // Burst time
        int[] pr = new int[n];   // Priority
        int[] ct = new int[n];   // Completion time
        int[] tat = new int[n];  // Turnaround time
        int[] wt = new int[n];   // Waiting time

        // Input process data
        for (int i = 0; i < n; i++) {
            pid[i] = i + 1;
            System.out.print("Enter Arrival Time for P" + pid[i] + ": ");
            at[i] = sc.nextInt();
            System.out.print("Enter Burst Time for P" + pid[i] + ": ");
            bt[i] = sc.nextInt();
            System.out.print("Enter Priority for P" + pid[i] + ": ");
            pr[i] = sc.nextInt();
        }

        boolean[] done = new boolean[n];
        int completed = 0, time = 0;
        double totalTAT = 0, totalWT = 0;

        // For Gantt chart
        StringBuilder ganttProcess = new StringBuilder();
        StringBuilder ganttTime = new StringBuilder("0");

        // Scheduling loop
        while (completed < n) {
            int idx = -1;
            int maxPriority = Integer.MIN_VALUE;

            // Find process with highest priority that has arrived
            for (int i = 0; i < n; i++) {
                if (!done[i] && at[i] <= time && pr[i] > maxPriority) {
                    maxPriority = pr[i];
                    idx = i;
                }
            }

            if (idx != -1) {
                time += bt[idx];
                ct[idx] = time;
                tat[idx] = ct[idx] - at[idx];
                wt[idx] = tat[idx] - bt[idx];
                done[idx] = true;
                completed++;

                ganttProcess.append(" | P").append(pid[idx]);
                ganttTime.append("   ").append(time);
            } else {
                time++;
            }
        }

        // Display process table
        System.out.println("\n--- Process Table ---");
        System.out.println("PID\tAT\tBT\tPR\tCT\tTAT\tWT");
        for (int i = 0; i < n; i++) {
            System.out.println(pid[i] + "\t" + at[i] + "\t" + bt[i] + "\t" + pr[i] + "\t" +
                    ct[i] + "\t" + tat[i] + "\t" + wt[i]);
            totalTAT += tat[i];
            totalWT += wt[i];
        }

        // Display averages
        System.out.printf("\nAverage Turnaround Time: %.2f", totalTAT / n);
        System.out.printf("\nAverage Waiting Time: %.2f\n", totalWT / n);

        // Display Gantt chart
        System.out.println("\n--- Gantt Chart ---");
        System.out.println(ganttProcess);
        System.out.println(ganttTime);

        sc.close();
    }
}
 */