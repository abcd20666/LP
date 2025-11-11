import java.io.*;
import java.util.*;

public class pass2 {
    public static void main(String[] args) throws IOException {
        // Load Symbol and Literal Tables
        Map<Integer, Integer> symbolAddr = loadTable("SYMTAB.txt");
        Map<Integer, Integer> literalAddr = loadTable("LITTAB.txt");

        BufferedReader icReader = new BufferedReader(new FileReader("IC.txt"));
        BufferedWriter bw = new BufferedWriter(new FileWriter("MachineCode.txt"));

        String line;
        int lc = 0;

        while ((line = icReader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split("\\s+");

            // Handle Assembler Directives (AD)
            if (parts[0].equals("(AD,01)")) { // START
                lc = Integer.parseInt(parts[1].substring(3, parts[1].length() - 1)); // (C,100)
                continue;
            } else if (parts[0].equals("(AD,02)") || parts[0].equals("(AD,03)")) { // END/ORIGIN
                continue;
            }

            // Handle Declarative Statements (DL)
            if (parts[0].equals("(DL,01)")) { // DC
                String constant = parts[1].substring(3, parts[1].length() - 1);
                bw.write(lc + "\t00\t0\t" + constant + "\n");
                lc++;
                continue;
            } else if (parts[0].equals("(DL,02)")) { // DS
                lc++;
                continue;
            }

            // Handle Imperative Statements (IS)
            if (parts[0].startsWith("(IS,")) {
                String opcode = parts[0].substring(4, parts[0].length() - 1); // e.g. (IS,04) -> 04
                String reg = "0";
                String mem = "000";

                // Register part (if present)
                if (parts.length >= 2 && parts[1].matches("\\d")) {
                    reg = parts[1];
                }

                // Memory operand part
                if (parts.length >= 3) {
                    String operand = parts[2];
                    if (operand.startsWith("(S,")) {
                        int index = Integer.parseInt(operand.substring(3, operand.length() - 1));
                        mem = String.format("%03d", symbolAddr.getOrDefault(index, 0));
                    } else if (operand.startsWith("(L,")) {
                        int index = Integer.parseInt(operand.substring(3, operand.length() - 1));
                        mem = String.format("%03d", literalAddr.getOrDefault(index, 0));
                    } else if (operand.startsWith("(C,")) {
                        mem = operand.substring(3, operand.length() - 1);
                    }
                }

                bw.write(lc + "\t" + opcode + "\t" + reg + "\t" + mem + "\n");
                lc++;
            }
        }

        icReader.close();
        bw.close();

        System.out.println("✅ Pass 2 completed successfully. Output → MachineCode.txt");
    }

    // Helper function to load SYMTAB and LITTAB
    static Map<Integer, Integer> loadTable(String filename) throws IOException {
        Map<Integer, Integer> table = new LinkedHashMap<>();
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;
        int index = 1;
        while ((line = br.readLine()) != null) {
            String[] parts = line.trim().split("\\s+");
            if (parts.length >= 2) {
                try {
                    table.put(index++, Integer.parseInt(parts[1]));
                } catch (NumberFormatException e) {
                    System.out.println("⚠ Invalid number format in " + filename + ": " + line);
                }
            }
        }
        br.close();
        return table;
    }
}
