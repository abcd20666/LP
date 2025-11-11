import java.util.*;

public class LRUPageReplacement {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter the number of pages: ");
        int n = sc.nextInt();
        int[] pages = new int[n];

        System.out.println("Enter the page reference string:");
        for (int i = 0; i < n; i++) {
            pages[i] = sc.nextInt();
        }

        System.out.print("Enter the number of frames: ");
        int capacity = sc.nextInt();

        ArrayList<Integer> frames = new ArrayList<>(capacity);
        int pageFaults = 0;

        System.out.println("\nPage Replacement Process:");

        for (int i = 0; i < n; i++) {
            int currentPage = pages[i];

            // If page is already in frame → no fault
            if (frames.contains(currentPage)) {
                frames.remove((Integer) currentPage);
                frames.add(currentPage);
            } else {
                // Page fault occurs
                if (frames.size() == capacity) {
                    frames.remove(0); // remove least recently used
                }
                frames.add(currentPage);
                pageFaults++;
            }

            System.out.print("Page " + currentPage + " -> ");
            for (int page : frames) {
                System.out.print(page + " ");
            }
            System.out.println();
        }

        System.out.println("\nTotal Page Faults: " + pageFaults);
        sc.close();
    }
}

/*Enter the number of pages: 8
Enter the page reference string:
1 2 3 4 1 2 5 1
Enter the number of frames: 3

Page Replacement Process:
Page 1 -> 1 
Page 2 -> 1 2
Page 3 -> 1 2 3
Page 4 -> 2 3 4
Page 1 -> 3 4 1
Page 2 -> 4 1 2
Page 5 -> 1 2 5
Page 1 -> 2 5 1

Total Page Faults: 7
 */


 /*import java.util.*;

public class LRUPageReplacement {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter the number of pages: ");
        int n = sc.nextInt();
        int[] pages = new int[n];

        System.out.println("Enter the page reference string:");
        for (int i = 0; i < n; i++) {
            pages[i] = sc.nextInt();
        }

        System.out.print("Enter the number of frames: ");
        int capacity = sc.nextInt();

        ArrayList<Integer> frames = new ArrayList<>(capacity);
        int pageFaults = 0;
        int pageHits = 0;

        System.out.println("\nPage Replacement Process:");

        for (int i = 0; i < n; i++) {
            int currentPage = pages[i];

            // Page Hit
            if (frames.contains(currentPage)) {
                frames.remove((Integer) currentPage);
                frames.add(currentPage); // move to most recently used
                pageHits++;
            } 
            // Page Fault
            else {
                if (frames.size() == capacity) {
                    frames.remove(0); // remove least recently used
                }
                frames.add(currentPage);
                pageFaults++;
            }

            // Display frame contents
            System.out.print("Page " + currentPage + " -> ");
            for (int page : frames) {
                System.out.print(page + " ");
            }
            System.out.println();
        }

        // Calculations
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