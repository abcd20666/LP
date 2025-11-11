import java.util.*;

public class PSPLow {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("*** Priority Scheduling (Preemptive, Lower = Higher Priority) ***");
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        int[] at = new int[n];   // Arrival time
        int[] bt = new int[n];   // Burst time
        int[] rt = new int[n];   // Remaining time
        int[] pr = new int[n];   // Priority
        int[] ct = new int[n];   // Completion time
        int[] tat = new int[n];  // Turnaround time
        int[] wt = new int[n];   // Waiting time
        boolean[] done = new boolean[n];

        for (int i = 0; i < n; i++) {
            System.out.print("Enter Arrival Time for P" + (i + 1) + ": ");
            at[i] = sc.nextInt();
            System.out.print("Enter Burst Time for P" + (i + 1) + ": ");
            bt[i] = sc.nextInt();
            System.out.print("Enter Priority for P" + (i + 1) + ": ");
            pr[i] = sc.nextInt();
            rt[i] = bt[i];
            System.out.println();
        }

        int completed = 0, time = 0;
        double avgWT = 0, avgTAT = 0;

        while (completed < n) {
            int idx = -1, minPr = Integer.MAX_VALUE;

            // Find process with highest priority (lowest number)
            for (int i = 0; i < n; i++) {
                if (!done[i] && at[i] <= time && pr[i] < minPr && rt[i] > 0) {
                    minPr = pr[i];
                    idx = i;
                }
            }

            if (idx == -1) {
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

        System.out.println("\nProcess\tAT\tBT\tPR\tCT\tTAT\tWT");
        for (int i = 0; i < n; i++) {
            System.out.println("P" + (i + 1) + "\t" + at[i] + "\t" + bt[i] + "\t" +
                    pr[i] + "\t" + ct[i] + "\t" + tat[i] + "\t" + wt[i]);
        }

        System.out.printf("\nAverage Turnaround Time: %.2f", avgTAT / n);
        System.out.printf("\nAverage Waiting Time: %.2f\n", avgWT / n);

        sc.close();
    }
}

/**** Priority Scheduling (Preemptive, Lower = Higher Priority) ***
Enter number of processes: 4
Enter Arrival Time for P1: 0
Enter Burst Time for P1: 5
Enter Priority for P1: 4

Enter Arrival Time for P2: 1
Enter Burst Time for P2: 4
Enter Priority for P2: 3

Enter Arrival Time for P3: 2
Enter Burst Time for P3: 2
Enter Priority for P3: 2

Enter Arrival Time for P4: 4
Enter Burst Time for P4: 1
Enter Priority for P4: 1


Process AT      BT      PR      CT      TAT     WT
P1      0       5       4       12      12      7
P2      1       4       3       8       7       3
P3      2       2       2       4       2       0
P4      4       1       1       5       1       0

Average Turnaround Time: 5.50
Average Waiting Time: 2.50 */

/*import java.util.*;

public class PSPLow {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("*** Priority Scheduling (Preemptive, Lower = Higher Priority) ***");
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        int[] at = new int[n];   // Arrival time
        int[] bt = new int[n];   // Burst time
        int[] rt = new int[n];   // Remaining time
        int[] pr = new int[n];   // Priority
        int[] ct = new int[n];   // Completion time
        int[] tat = new int[n];  // Turnaround time
        int[] wt = new int[n];   // Waiting time
        boolean[] done = new boolean[n];

        for (int i = 0; i < n; i++) {
            System.out.print("Enter Arrival Time for P" + (i + 1) + ": ");
            at[i] = sc.nextInt();
            System.out.print("Enter Burst Time for P" + (i + 1) + ": ");
            bt[i] = sc.nextInt();
            System.out.print("Enter Priority for P" + (i + 1) + ": ");
            pr[i] = sc.nextInt();
            rt[i] = bt[i];
            System.out.println();
        }

        int completed = 0, time = 0;
        double avgWT = 0, avgTAT = 0;

        // For Gantt chart
        ArrayList<String> ganttProcess = new ArrayList<>();
        ArrayList<Integer> ganttTime = new ArrayList<>();
        ganttTime.add(0);

        while (completed < n) {
            int idx = -1, minPr = Integer.MAX_VALUE;

            // Find process with highest priority (lowest number)
            for (int i = 0; i < n; i++) {
                if (!done[i] && at[i] <= time && pr[i] < minPr && rt[i] > 0) {
                    minPr = pr[i];
                    idx = i;
                }
            }

            if (idx == -1) {
                time++;
            } else {
                rt[idx]--;
                time++;
                ganttProcess.add("P" + (idx + 1));
                ganttTime.add(time);

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

        // Display process table
        System.out.println("\nProcess\tAT\tBT\tPR\tCT\tTAT\tWT");
        for (int i = 0; i < n; i++) {
            System.out.println("P" + (i + 1) + "\t" + at[i] + "\t" + bt[i] + "\t" +
                    pr[i] + "\t" + ct[i] + "\t" + tat[i] + "\t" + wt[i]);
        }

        System.out.printf("\nAverage Turnaround Time: %.2f", avgTAT / n);
        System.out.printf("\nAverage Waiting Time: %.2f\n", avgWT / n);

        // Display Gantt chart
        System.out.println("\n--- Gantt Chart ---");
        for (String p : ganttProcess) {
            System.out.print("| " + p + " ");
        }
        System.out.println("|");

        for (int t : ganttTime) {
            System.out.print(t + "\t");
        }
        System.out.println();

        sc.close();
    }
}
 */