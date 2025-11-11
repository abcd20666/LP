import java.util.*;

public class OptimalPageReplacement {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter the total number of frames: ");
        int frames = sc.nextInt();

        System.out.print("Enter the reference string size: ");
        int n = sc.nextInt();

        int[] ref = new int[n];
        System.out.println("Enter the reference string:");
        for (int i = 0; i < n; i++) {
            ref[i] = sc.nextInt();
        }

        int[] buffer = new int[frames];
        Arrays.fill(buffer, -1);

        int fault = 0;

        System.out.println("\nPage Replacement Process:");

        for (int i = 0; i < n; i++) {
            int page = ref[i];
            boolean found = false;

            // Check if page is already in frame (hit)
            for (int j = 0; j < frames; j++) {
                if (buffer[j] == page) {
                    found = true;
                    break;
                }
            }

            // If page not found (fault)
            if (!found) {
                fault++;
                int replaceIndex = -1;

                // Find empty frame first
                for (int j = 0; j < frames; j++) {
                    if (buffer[j] == -1) {
                        replaceIndex = j;
                        break;
                    }
                }

                // If no empty frame, find the page that will not be used for longest time
                if (replaceIndex == -1) {
                    int farthest = -1;
                    for (int j = 0; j < frames; j++) {
                        int nextUse = Integer.MAX_VALUE;
                        for (int k = i + 1; k < n; k++) {
                            if (buffer[j] == ref[k]) {
                                nextUse = k;
                                break;
                            }
                        }
                        if (nextUse > farthest) {
                            farthest = nextUse;
                            replaceIndex = j;
                        }
                    }
                }

                buffer[replaceIndex] = page;
            }

            // Display current frame status
            System.out.print("Page " + page + " -> ");
            for (int b : buffer) {
                if (b == -1)
                    System.out.print("- ");
                else
                    System.out.print(b + " ");
            }
            System.out.println();
        }

        System.out.println("\nTotal Page Faults: " + fault);

        sc.close();
    }
}
/*Enter the total number of frames: 3
Enter the reference string size: 8
Enter the reference string:
1 2 3 4 1 2 5 1

Page Replacement Process:
Page 1 -> 1 - -
Page 2 -> 1 2 -
Page 3 -> 1 2 3 
Page 4 -> 1 2 4
Page 1 -> 1 2 4
Page 2 -> 1 2 4
Page 5 -> 1 5 4
Page 1 -> 1 5 4

Total Page Faults: 5 */


/*import java.util.*;

public class OptimalPageReplacement {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter the total number of frames: ");
        int frames = sc.nextInt();

        System.out.print("Enter the reference string size: ");
        int n = sc.nextInt();

        int[] ref = new int[n];
        System.out.println("Enter the reference string:");
        for (int i = 0; i < n; i++) {
            ref[i] = sc.nextInt();
        }

        int[] buffer = new int[frames];
        Arrays.fill(buffer, -1);

        int pageFaults = 0;
        int pageHits = 0;

        System.out.println("\nPage Replacement Process:");

        for (int i = 0; i < n; i++) {
            int page = ref[i];
            boolean found = false;

            // Check if page is already in frame (hit)
            for (int j = 0; j < frames; j++) {
                if (buffer[j] == page) {
                    found = true;
                    break;
                }
            }

            // If not found → Page Fault
            if (!found) {
                pageFaults++;
                int replaceIndex = -1;

                // Check for empty frame first
                for (int j = 0; j < frames; j++) {
                    if (buffer[j] == -1) {
                        replaceIndex = j;
                        break;
                    }
                }

                // If no empty frame, find the page that will not be used for the longest time
                if (replaceIndex == -1) {
                    int farthest = -1;
                    for (int j = 0; j < frames; j++) {
                        int nextUse = Integer.MAX_VALUE;
                        for (int k = i + 1; k < n; k++) {
                            if (buffer[j] == ref[k]) {
                                nextUse = k;
                                break;
                            }
                        }
                        if (nextUse > farthest) {
                            farthest = nextUse;
                            replaceIndex = j;
                        }
                    }
                }

                buffer[replaceIndex] = page;
            } 
            // If found → Page Hit
            else {
                pageHits++;
            }

            // Display current frame status
            System.out.print("Page " + page + " -> ");
            for (int b : buffer) {
                if (b == -1)
                    System.out.print("- ");
                else
                    System.out.print(b + " ");
            }
            System.out.println();
        }

        // Calculate ratios
        float hitRatio = (float) pageHits / n;
        float faultRatio = (float) pageFaults / n;

        System.out.println("\nTotal Page Faults: " + pageFaults);
        System.out.println("Total Page Hits: " + pageHits);
        System.out.printf("Hit Ratio: %.3f\n", hitRatio);
        System.out.printf("Page Fault Ratio: %.3f\n", faultRatio);

        sc.close();
    }
}
 */