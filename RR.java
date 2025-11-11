import java.util.*;

public class RR{
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("*** Round Robin CPU Scheduling ***");
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        int[] pid = new int[n];        // Process ID
        int[] at = new int[n];         // Arrival Time
        int[] bt = new int[n];         // Burst Time
        int[] rt = new int[n];         // Remaining Time
        int[] ct = new int[n];         // Completion Time
        int[] tat = new int[n];        // Turnaround Time
        int[] wt = new int[n];         // Waiting Time

        for (int i = 0; i < n; i++) {
            pid[i] = i + 1;
            System.out.print("Enter Arrival Time for P" + pid[i] + ": ");
            at[i] = sc.nextInt();
            System.out.print("Enter Burst Time for P" + pid[i] + ": ");
            bt[i] = sc.nextInt();
            rt[i] = bt[i]; // initialize remaining time
            System.out.println();
        }

        System.out.print("Enter Time Quantum: ");
        int quantum = sc.nextInt();

        int time = 0, completed = 0;
        Queue<Integer> q = new LinkedList<>();

        // Sort by arrival time
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (at[i] > at[j]) {
                    int temp;
                    temp = at[i]; at[i] = at[j]; at[j] = temp;
                    temp = bt[i]; bt[i] = bt[j]; bt[j] = temp;
                    temp = rt[i]; rt[i] = rt[j]; rt[j] = temp;
                    temp = pid[i]; pid[i] = pid[j]; pid[j] = temp;
                }
            }
        }

        q.add(0);
        boolean[] visited = new boolean[n];
        visited[0] = true;
        time = at[0];

        while (!q.isEmpty()) {
            int i = q.poll();

            if (rt[i] <= quantum) {
                time += rt[i];
                rt[i] = 0;
                ct[i] = time;
                completed++;
            } else {
                rt[i] -= quantum;
                time += quantum;
            }

            // Add newly arrived processes to queue
            for (int j = 0; j < n; j++) {
                if (at[j] <= time && rt[j] > 0 && !visited[j]) {
                    q.add(j);
                    visited[j] = true;
                }
            }

            // Re-add current process if not finished
            if (rt[i] > 0)
                q.add(i);
        }

        // Calculate Turnaround Time and Waiting Time
        double totalTAT = 0, totalWT = 0;
        for (int i = 0; i < n; i++) {
            tat[i] = ct[i] - at[i];
            wt[i] = tat[i] - bt[i];
            totalTAT += tat[i];
            totalWT += wt[i];
        }

        // Display results
        System.out.println("\nProcess\tAT\tBT\tCT\tTAT\tWT");
        System.out.println("-------------------------------------");
        for (int i = 0; i < n; i++) {
            System.out.println("P" + pid[i] + "\t" + at[i] + "\t" + bt[i] + "\t" +
                               ct[i] + "\t" + tat[i] + "\t" + wt[i]);
        }

        System.out.printf("\nAverage Turnaround Time = %.2f", totalTAT / n);
        System.out.printf("\nAverage Waiting Time = %.2f\n", totalWT / n);

        sc.close();
    }
}

/**** Round Robin CPU Scheduling ***
Enter number of processes: 4
Enter Arrival Time for P1: 0
Enter Burst Time for P1: 5

Enter Arrival Time for P2: 1
Enter Burst Time for P2: 4

Enter Arrival Time for P3: 2
Enter Burst Time for P3: 2

Enter Arrival Time for P4: 4
Enter Burst Time for P4: 1

Enter Time Quantum: 2

Process AT      BT      CT      TAT     WT
-------------------------------------    
P1      0       5       12      12      7
P2      1       4       11      10      6
P3      2       2       6       4       2
P4      4       1       9       5       4

Average Turnaround Time = 7.75
Average Waiting Time = 4.75 */

/*import java.util.*;

public class RR {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("*** Round Robin CPU Scheduling ***");
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        int[] pid = new int[n];
        int[] at = new int[n];
        int[] bt = new int[n];
        int[] rt = new int[n];
        int[] ct = new int[n];
        int[] tat = new int[n];
        int[] wt = new int[n];

        for (int i = 0; i < n; i++) {
            pid[i] = i + 1;
            System.out.print("Enter Arrival Time for P" + pid[i] + ": ");
            at[i] = sc.nextInt();
            System.out.print("Enter Burst Time for P" + pid[i] + ": ");
            bt[i] = sc.nextInt();
            rt[i] = bt[i];
            System.out.println();
        }

        System.out.print("Enter Time Quantum: ");
        int quantum = sc.nextInt();

        // Sort processes by arrival time
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (at[i] > at[j]) {
                    int temp;
                    temp = at[i]; at[i] = at[j]; at[j] = temp;
                    temp = bt[i]; bt[i] = bt[j]; bt[j] = temp;
                    temp = rt[i]; rt[i] = rt[j]; rt[j] = temp;
                    temp = pid[i]; pid[i] = pid[j]; pid[j] = temp;
                }
            }
        }

        Queue<Integer> q = new LinkedList<>();
        boolean[] visited = new boolean[n];
        ArrayList<String> ganttProcess = new ArrayList<>();
        ArrayList<Integer> ganttTime = new ArrayList<>();

        int time = at[0];
        q.add(0);
        visited[0] = true;
        ganttTime.add(time);

        while (!q.isEmpty()) {
            int i = q.poll();

            if (rt[i] <= quantum) {
                time += rt[i];
                rt[i] = 0;
                ct[i] = time;
            } else {
                rt[i] -= quantum;
                time += quantum;
            }

            ganttProcess.add("P" + pid[i]);
            ganttTime.add(time);

            // Add newly arrived processes
            for (int j = 0; j < n; j++) {
                if (at[j] <= time && rt[j] > 0 && !visited[j]) {
                    q.add(j);
                    visited[j] = true;
                }
            }

            // Re-add current process if not finished
            if (rt[i] > 0)
                q.add(i);
        }

        // Calculate TAT and WT
        double totalTAT = 0, totalWT = 0;
        for (int i = 0; i < n; i++) {
            tat[i] = ct[i] - at[i];
            wt[i] = tat[i] - bt[i];
            totalTAT += tat[i];
            totalWT += wt[i];
        }

        // Display Results
        System.out.println("\nProcess\tAT\tBT\tCT\tTAT\tWT");
        System.out.println("-------------------------------------");
        for (int i = 0; i < n; i++) {
            System.out.println("P" + pid[i] + "\t" + at[i] + "\t" + bt[i] + "\t" +
                               ct[i] + "\t" + tat[i] + "\t" + wt[i]);
        }

        System.out.printf("\nAverage Turnaround Time = %.2f", totalTAT / n);
        System.out.printf("\nAverage Waiting Time = %.2f\n", totalWT / n);

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

        sc.close();
    }
}
 */