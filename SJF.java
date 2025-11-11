import java.util.*;

public class SJF {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("*** Shortest Job First Scheduling (Preemptive) ***");
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        int[] at = new int[n];   // Arrival time
        int[] bt = new int[n];   // Burst time
        int[] rt = new int[n];   // Remaining time
        int[] ct = new int[n];   // Completion time
        int[] tat = new int[n];  // Turnaround time
        int[] wt = new int[n];   // Waiting time
        boolean[] done = new boolean[n];

        for (int i = 0; i < n; i++) {
            System.out.print("Enter Arrival Time for P" + (i + 1) + ": ");
            at[i] = sc.nextInt();
            System.out.print("Enter Burst Time for P" + (i + 1) + ": ");
            bt[i] = sc.nextInt();
            rt[i] = bt[i];
            System.out.println();
        }

        int completed = 0, time = 0;
        double avgTAT = 0, avgWT = 0;

        while (completed < n) {
            int idx = -1, minRT = Integer.MAX_VALUE;

            // Find process with minimum remaining time among arrived ones
            for (int i = 0; i < n; i++) {
                if (!done[i] && at[i] <= time && rt[i] < minRT && rt[i] > 0) {
                    minRT = rt[i];
                    idx = i;
                }
            }

            if (idx == -1) { // No process available
                time++;
            } else {
                rt[idx]--;
                time++;

                if (rt[idx] == 0) {
                    done[idx] = true;
                    completed++;
                    ct[idx] = time;
                    tat[idx] = ct[idx] - at[idx];
                    wt[idx] = tat[idx] - bt[idx];
                    avgTAT += tat[idx];
                    avgWT += wt[idx];
                }
            }
        }

        System.out.println("\nProcess\tAT\tBT\tCT\tTAT\tWT");
        for (int i = 0; i < n; i++) {
            System.out.println("P" + (i + 1) + "\t" + at[i] + "\t" + bt[i] + "\t" +
                    ct[i] + "\t" + tat[i] + "\t" + wt[i]);
        }

        System.out.printf("\nAverage Turnaround Time: %.2f", avgTAT / n);
        System.out.printf("\nAverage Waiting Time: %.2f\n", avgWT / n);

        sc.close();
    }
}
 

/*
*** Shortest Job First Scheduling (Preemptive) ***
Enter number of processes: 4
Enter Arrival Time for P1: 0
Enter Burst Time for P1: 5

Enter Arrival Time for P2: 1
Enter Burst Time for P2: 3

Enter Arrival Time for P3: 2
Enter Burst Time for P3: 4

Enter Arrival Time for P4: 4
Enter Burst Time for P4: 1


Process AT      BT      CT      TAT     WT
P1      0       5       9       9       4
P2      1       3       4       3       0
P3      2       4       13      11      7
P4      4       1       5       1       0

Average Turnaround Time: 6.00
Average Waiting Time: 2.75
*/

/*import java.util.*;

public class SJF_Preemptive {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("*** Shortest Job First Scheduling (Preemptive) ***");
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        int[] at = new int[n];   // Arrival Time
        int[] bt = new int[n];   // Burst Time
        int[] rt = new int[n];   // Remaining Time
        int[] ct = new int[n];   // Completion Time
        int[] tat = new int[n];  // Turnaround Time
        int[] wt = new int[n];   // Waiting Time
        boolean[] done = new boolean[n];

        for (int i = 0; i < n; i++) {
            System.out.print("Enter Arrival Time for P" + (i + 1) + ": ");
            at[i] = sc.nextInt();
            System.out.print("Enter Burst Time for P" + (i + 1) + ": ");
            bt[i] = sc.nextInt();
            rt[i] = bt[i];
            System.out.println();
        }

        int completed = 0, time = 0;
        double totalTAT = 0, totalWT = 0;

        ArrayList<String> gantt = new ArrayList<>(); // Gantt chart list

        while (completed < n) {
            int idx = -1, minRT = Integer.MAX_VALUE;

            // Select process with minimum remaining time among arrived ones
            for (int i = 0; i < n; i++) {
                if (!done[i] && at[i] <= time && rt[i] < minRT && rt[i] > 0) {
                    minRT = rt[i];
                    idx = i;
                }
            }

            if (idx == -1) { // CPU idle
                time++;
                gantt.add("Idle");
            } else {
                gantt.add("P" + (idx + 1));
                rt[idx]--;
                time++;

                if (rt[idx] == 0) {
                    done[idx] = true;
                    completed++;
                    ct[idx] = time;
                    tat[idx] = ct[idx] - at[idx];
                    wt[idx] = tat[idx] - bt[idx];
                    totalTAT += tat[idx];
                    totalWT += wt[idx];
                }
            }
        }

        // Display Table
        System.out.println("\nProcess\tAT\tBT\tCT\tTAT\tWT");
        for (int i = 0; i < n; i++) {
            System.out.println("P" + (i + 1) + "\t" + at[i] + "\t" + bt[i] + "\t" +
                    ct[i] + "\t" + tat[i] + "\t" + wt[i]);
        }

        System.out.printf("\nAverage Turnaround Time: %.2f", totalTAT / n);
        System.out.printf("\nAverage Waiting Time: %.2f\n", totalWT / n);

        // Display Gantt Chart
        System.out.println("\nGantt Chart:");
        for (String p : gantt)
            System.out.print("| " + p + " ");
        System.out.println("|");

        System.out.print("0");
        for (int i = 1; i <= gantt.size(); i++)
            System.out.print("  " + i);
        System.out.println();

        sc.close();
    }
}
 */