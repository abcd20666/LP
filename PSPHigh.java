import java.util.Scanner;

public class PSPHigh {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("*** Priority Scheduling (Preemptive - Higher number = Higher priority) ***");
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        int[] pid = new int[n];
        int[] arrival = new int[n];
        int[] burst = new int[n];
        int[] burstCopy = new int[n];
        int[] priority = new int[n];
        int[] completion = new int[n];
        int[] tat = new int[n];
        int[] wait = new int[n];

        for (int i = 0; i < n; i++) {
            pid[i] = i + 1;
            System.out.println("\nProcess " + pid[i] + ":");
            System.out.print("Arrival Time: ");
            arrival[i] = sc.nextInt();
            System.out.print("Burst Time: ");
            burst[i] = sc.nextInt();
            System.out.print("Priority: ");
            priority[i] = sc.nextInt();
            burstCopy[i] = burst[i];
        }

        int completed = 0, time = 0;
        double totalTAT = 0, totalWT = 0;

        System.out.println("\n--- Gantt Chart ---");
        while (completed != n) {
            int idx = -1;
            int highestPriority = -1;

            for (int i = 0; i < n; i++) {
                if (arrival[i] <= time && burst[i] > 0 && priority[i] > highestPriority) {
                    highestPriority = priority[i];
                    idx = i;
                }
            }

            if (idx != -1) {
                System.out.print("P" + pid[idx] + " ");
                burst[idx]--;

                if (burst[idx] == 0) {
                    completed++;
                    completion[idx] = time + 1;
                    tat[idx] = completion[idx] - arrival[idx];
                    wait[idx] = tat[idx] - burstCopy[idx];
                    totalTAT += tat[idx];
                    totalWT += wait[idx];
                }
            }
            time++;
        }

        System.out.println("\n\n--- Final Results ---");
        System.out.println("PID\tAT\tBT\tPR\tCT\tTAT\tWT");
        for (int i = 0; i < n; i++) {
            System.out.println(pid[i] + "\t" + arrival[i] + "\t" + burstCopy[i] + "\t" + priority[i] + "\t"
                    + completion[i] + "\t" + tat[i] + "\t" + wait[i]);
        }

        System.out.printf("\nAverage Turnaround Time: %.2f", totalTAT / n);
        System.out.printf("\nAverage Waiting Time: %.2f\n", totalWT / n);

        sc.close();
    }
}

/**** Priority Scheduling (Preemptive - Higher number = Higher priority) ***
Enter number of processes: 4

Process 1:
Arrival Time: 0
Burst Time: 5
Priority: 1

Process 2:
Arrival Time: 1
Burst Time: 4
Priority: 2

Process 3:
Arrival Time: 2
Burst Time: 2
Priority: 3

Process 4:
Arrival Time: 4
Burst Time: 1
Priority: 4

--- Gantt Chart ---
P1 P2 P3 P3 P4 P2 P2 P2 P1 P1 P1 P1

--- Final Results ---
PID     AT      BT      PR      CT      TAT     WT
1       0       5       1       12      12      7
2       1       4       2       8       7       3
3       2       2       3       4       2       0
4       4       1       4       5       1       0

Average Turnaround Time: 5.50
Average Waiting Time: 2.50 */


/* import java.util.*;

public class PSPHigh {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("*** Priority Scheduling (Preemptive - Higher number = Higher Priority) ***");
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        int[] pid = new int[n];
        int[] arrival = new int[n];
        int[] burst = new int[n];
        int[] burstCopy = new int[n];
        int[] priority = new int[n];
        int[] completion = new int[n];
        int[] tat = new int[n];
        int[] wait = new int[n];

        for (int i = 0; i < n; i++) {
            pid[i] = i + 1;
            System.out.println("\nProcess " + pid[i] + ":");
            System.out.print("Arrival Time: ");
            arrival[i] = sc.nextInt();
            System.out.print("Burst Time: ");
            burst[i] = sc.nextInt();
            System.out.print("Priority: ");
            priority[i] = sc.nextInt();
            burstCopy[i] = burst[i];
        }

        int completed = 0, time = 0;
        double totalTAT = 0, totalWT = 0;

        // Gantt chart data
        ArrayList<String> ganttProcess = new ArrayList<>();
        ArrayList<Integer> ganttTime = new ArrayList<>();
        ganttTime.add(0);

        while (completed != n) {
            int idx = -1;
            int highestPriority = -1;

            for (int i = 0; i < n; i++) {
                if (arrival[i] <= time && burst[i] > 0 && priority[i] > highestPriority) {
                    highestPriority = priority[i];
                    idx = i;
                }
            }

            if (idx != -1) {
                if (ganttProcess.isEmpty() || !ganttProcess.get(ganttProcess.size() - 1).equals("P" + pid[idx])) {
                    ganttProcess.add("P" + pid[idx]);
                    ganttTime.add(time);
                }

                burst[idx]--;

                if (burst[idx] == 0) {
                    completed++;
                    completion[idx] = time + 1;
                    tat[idx] = completion[idx] - arrival[idx];
                    wait[idx] = tat[idx] - burstCopy[idx];
                    totalTAT += tat[idx];
                    totalWT += wait[idx];
                }
            }
            time++;
        }
        ganttTime.add(time);

        // Final results
        System.out.println("\n--- Final Results ---");
        System.out.println("PID\tAT\tBT\tPR\tCT\tTAT\tWT");
        for (int i = 0; i < n; i++) {
            System.out.println(pid[i] + "\t" + arrival[i] + "\t" + burstCopy[i] + "\t" + priority[i] + "\t"
                    + completion[i] + "\t" + tat[i] + "\t" + wait[i]);
        }

        System.out.printf("\nAverage Turnaround Time: %.2f", totalTAT / n);
        System.out.printf("\nAverage Waiting Time: %.2f\n", totalWT / n);

        // Gantt chart table
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
