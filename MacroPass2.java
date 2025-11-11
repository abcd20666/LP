import java.io.*;
import java.util.*;

public class MacroPass2 {
    public static void main(String[] args) throws IOException {

        BufferedReader ir = new BufferedReader(new FileReader("intermediate.txt")); // Non-macro lines + macro calls
        BufferedReader mntFile = new BufferedReader(new FileReader("mnt.txt"));
        BufferedReader mdtFile = new BufferedReader(new FileReader("mdt.txt"));
        BufferedReader kpdtFile = new BufferedReader(new FileReader("kpdt.txt"));

        BufferedWriter output = new BufferedWriter(new FileWriter("pass2_output.txt"));

        // --- Data structures ---
        Map<String, MNTEntry> MNT = new HashMap<>();
        List<String> MDT = new ArrayList<>();
        List<String[]> KPDT = new ArrayList<>();

        // --- Read MDT ---
        String line;
        while ((line = mdtFile.readLine()) != null) {
            MDT.add(line.trim());
        }

        // --- Read KPDTAB ---
        while ((line = kpdtFile.readLine()) != null) {
            KPDT.add(line.trim().split("\\s+"));
        }

        // --- Read MNT (skip parameter name like &X or &P) ---
        while ((line = mntFile.readLine()) != null) {
            String[] parts = line.trim().split("\\s+");

            // MNT format: MacroName  &ParamName  PP  KP  MDTP  KPDT
            if (parts.length < 6) {
                System.err.println("⚠️ Invalid MNT entry: " + line);
                continue;
            }

            String name = parts[0];
            int pp = Integer.parseInt(parts[2]);
            int kp = Integer.parseInt(parts[3]);
            int mdtp = Integer.parseInt(parts[4]);
            int kpdtp = Integer.parseInt(parts[5]);

            MNT.put(name, new MNTEntry(name, pp, kp, mdtp, kpdtp));
        }

        // --- Process Intermediate file ---
        while ((line = ir.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;

            String[] tokens = line.split("[ ,]+");
            String macroName = tokens[0];

            if (!MNT.containsKey(macroName)) {
                // Normal instruction (not a macro call)
                output.write(line + "\n");
                continue;
            }

            // --- Macro call detected ---
            MNTEntry entry = MNT.get(macroName);
            int pp = entry.pp;
            int kp = entry.kp;
            int mdtp = entry.mdtp - 1; // Convert to 0-based index
            int kpdtp = entry.kpdtp - 1;

            // --- Create APTAB ---
            String[] APTAB = new String[pp + kp + 1];
            int i = 1;

            // Positional parameters
            for (int j = 1; j <= pp && i < tokens.length; j++, i++) {
                APTAB[j] = tokens[i];
            }

            // Keyword parameters (defaults from KPDT)
            for (int j = 0; j < kp; j++) {
                if (kpdtp + j < KPDT.size()) {
                    APTAB[pp + j + 1] = KPDT.get(kpdtp + j)[1];
                }
            }

            // Overridden keyword parameters
            while (i < tokens.length) {
                String[] kv = tokens[i].split("=");
                String key = kv[0].replace("&", "");
                String value = kv[1];
                for (int j = 0; j < kp; j++) {
                    if (KPDT.get(kpdtp + j)[0].equals("&" + key)) {
                        APTAB[pp + j + 1] = value;
                    }
                }
                i++;
            }

            // --- Expand macro ---
            int curr = mdtp;
            while (curr < MDT.size() && !MDT.get(curr).equalsIgnoreCase("MEND")) {
                String[] parts = MDT.get(curr).split("\\s+");
                output.write("+ " + parts[0]);
                for (int j = 1; j < parts.length; j++) {
                    if (parts[j].startsWith("(P,")) {
                        int idx = Integer.parseInt(parts[j].substring(3, parts[j].length() - 1));
                        output.write(" " + APTAB[idx]);
                    } else {
                        output.write(" " + parts[j]);
                    }
                }
                output.write("\n");
                curr++;
            }
        }

        ir.close();
        mntFile.close();
        mdtFile.close();
        kpdtFile.close();
        output.close();

        System.out.println("✅ Pass 2 of Macro Processor completed successfully!");
    }
}

// --- Helper class for MNT entries ---
class MNTEntry {
    String name;
    int pp, kp, mdtp, kpdtp;

    MNTEntry(String name, int pp, int kp, int mdtp, int kpdtp) {
        this.name = name;
        this.pp = pp;
        this.kp = kp;
        this.mdtp = mdtp;
        this.kpdtp = kpdtp;
    }
}
