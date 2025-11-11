import java.util.*;

public class FIFOPageReplacement {

    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of frames: ");
        int frames = sc.nextInt();

        System.out.print("Enter number of pages: ");
        int pages = sc.nextInt();

        int incomingStream[] = new int[pages];
        System.out.println("Enter the page reference string:");
        for (int i = 0; i < pages; i++) {
            incomingStream[i] = sc.nextInt();
        }

        int temp[] = new int[frames];
        Arrays.fill(temp, -1); // initialize all frames as empty
        int pageFaults = 0;
        int pointer = 0; // FIFO replacement pointer

        System.out.println("\nIncoming\tFrame1\t\tFrame2\t\tFrame3");

        for (int i = 0; i < pages; i++) {
            boolean hit = false;

            // check if page already exists (hit)
            for (int j = 0; j < frames; j++) {
                if (incomingStream[i] == temp[j]) {
                    hit = true;
                    break;
                }
            }

            // if not hit, replace oldest page
            if (!hit) {
                temp[pointer] = incomingStream[i];
                pointer = (pointer + 1) % frames;
                pageFaults++;
            }

            // display frame status
            System.out.print(incomingStream[i] + "\t\t");
            for (int j = 0; j < frames; j++) {
                if (temp[j] != -1)
                    System.out.print(temp[j] + "\t\t");
                else
                    System.out.print("-\t\t");
            }
            System.out.println();
        }

        System.out.println("\nTotal Page Faults: " + pageFaults);
        sc.close();
    }
}

/*Enter number of frames: 3
Enter number of pages: 8
Enter the page reference string:
1 2 3 4 1 2 5 1

Incoming        Frame1          Frame2          Frame3
1               1               -               -
2               1               2               -
3               1               2               3
4               4               2               3
1               4               1               3
2               4               1               2
5               5               1               2
1               5               1               2

Total Page Faults: 7
 */

 /*import java.util.*;

public class FIFOPageReplacement {

    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of frames: ");
        int frames = sc.nextInt();

        System.out.print("Enter number of pages: ");
        int pages = sc.nextInt();

        int incomingStream[] = new int[pages];
        System.out.println("Enter the page reference string:");
        for (int i = 0; i < pages; i++) {
            incomingStream[i] = sc.nextInt();
        }

        int temp[] = new int[frames];
        Arrays.fill(temp, -1); // initialize all frames as empty
        int pageFaults = 0;
        int pageHits = 0;
        int pointer = 0; // FIFO replacement pointer

        System.out.println("\nIncoming\tFrame1\t\tFrame2\t\tFrame3\t\tStatus");

        for (int i = 0; i < pages; i++) {
            boolean hit = false;

            // check if page already exists (hit)
            for (int j = 0; j < frames; j++) {
                if (incomingStream[i] == temp[j]) {
                    hit = true;
                    pageHits++;
                    break;
                }
            }

            // if not hit, replace oldest page
            if (!hit) {
                temp[pointer] = incomingStream[i];
                pointer = (pointer + 1) % frames;
                pageFaults++;
            }

            // display frame status
            System.out.print(incomingStream[i] + "\t\t");
            for (int j = 0; j < frames; j++) {
                if (temp[j] != -1)
                    System.out.print(temp[j] + "\t\t");
                else
                    System.out.print("-\t\t");
            }

            if (hit)
                System.out.print("HIT");
            else
                System.out.print("FAULT");

            System.out.println();
        }

        double hitRatio = (double) pageHits / pages;
        double faultRatio = (double) pageFaults / pages;

        System.out.println("\nTotal Page Faults: " + pageFaults);
        System.out.println("Total Page Hits:   " + pageHits);
        System.out.printf("Page Fault Ratio:  %.3f\n", faultRatio);
        System.out.printf("Hit Ratio:         %.3f\n", hitRatio);

        sc.close();
    }
}
 */