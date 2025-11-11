import java.io.*;
import java.util.*;

public class Assembler {

    // Predefined tables for assembler directives and instructions
    static Map<String, String> IS = new HashMap<>();
    static Map<String, String> DL = new HashMap<>();
    static Map<String, String> AD = new HashMap<>();

    static {
        // Imperative Statements
        IS.put("STOP", "00");
        IS.put("ADD", "01");
        IS.put("SUB", "02");
        IS.put("MULT", "03");
        IS.put("MOVER", "04");
        IS.put("MOVEM", "05");
        IS.put("COMP", "06");
        IS.put("BC", "07");
        IS.put("DIV", "08");
        IS.put("READ", "09");
        IS.put("PRINT", "10");

        // Declarative
        DL.put("DC", "01");
        DL.put("DS", "02");

        // Assembler Directives
        AD.put("START", "01");
        AD.put("END", "02");
        AD.put("ORIGIN", "03");
        AD.put("EQU", "04");
        AD.put("LTORG", "05");
    }

    public static void main(String[] args) throws IOException {
        pass1();
        pass2();
    }

    // ------------------- PASS 1 -------------------
    static void pass1() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("input.asm"));
        BufferedWriter ic = new BufferedWriter(new FileWriter("IC.txt"));
        BufferedWriter symtab = new BufferedWriter(new FileWriter("SYMTAB.txt"));
        BufferedWriter littab = new BufferedWriter(new FileWriter("LITTAB.txt"));

        Map<String, Integer> SYMTAB = new LinkedHashMap<>();
        Map<String, Integer> LITTAB = new LinkedHashMap<>();
        int lc = 0, symIndex = 1, litIndex = 1;

        String line;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;
            String[] parts = line.split("\\s+");

            // Label handling
            int i = 0;
            String label = null;
            if (!IS.containsKey(parts[0]) && !DL.containsKey(parts[0]) && !AD.containsKey(parts[0])) {
                label = parts[0];
                SYMTAB.put(label, lc);
                i++;
            }

            // START
            if (AD.containsKey(parts[i]) && parts[i].equals("START")) {
                lc = Integer.parseInt(parts[i + 1]);
                ic.write("(AD,01) (C," + lc + ")\n");
            }

            // END
            else if (AD.containsKey(parts[i]) && parts[i].equals("END")) {
                ic.write("(AD,02)\n");
            }

            // Declarative (DC / DS)
            else if (DL.containsKey(parts[i])) {
                if (parts[i].equals("DC")) {
                    ic.write("(DL,01) (C," + parts[i + 1] + ")\n");
                    lc++;
                } else if (parts[i].equals("DS")) {
                    ic.write("(DL,02) (C," + parts[i + 1] + ")\n");
                    lc += Integer.parseInt(parts[i + 1]);
                }
            }

            // Imperative Statements
            else if (IS.containsKey(parts[i])) {
                String opcode = IS.get(parts[i]);
                String reg = "0";
                String mem = "";

                if (parts.length > i + 1)
                    reg = getRegisterCode(parts[i + 1]);

                if (parts.length > i + 2) {
                    String operand = parts[i + 2];
                    if (operand.startsWith("=")) {  // literal
                        if (!LITTAB.containsKey(operand)) {
                            LITTAB.put(operand, -1);
                        }
                        mem = "(L," + litIndex++ + ")";
                    } else { // symbol
                        if (!SYMTAB.containsKey(operand)) {
                            SYMTAB.put(operand, -1);
                        }
                        int index = new ArrayList<>(SYMTAB.keySet()).indexOf(operand) + 1;
                        mem = "(S," + index + ")";
                    }
                }
                ic.write("(IS," + opcode + ") " + reg + " " + mem + "\n");
                lc++;
            }
        }

        // Assign literal addresses at the end of the program
        for (String lit : LITTAB.keySet()) {
            if (LITTAB.get(lit) == -1)
                LITTAB.put(lit, lc++);
        }

        // Write SYMTAB
        for (Map.Entry<String, Integer> e : SYMTAB.entrySet()) {
            symtab.write(e.getKey() + "\t" + e.getValue() + "\n");
        }

        // Write LITTAB
        for (Map.Entry<String, Integer> e : LITTAB.entrySet()) {
            littab.write(e.getKey() + "\t" + e.getValue() + "\n");
        }

        br.close();
        ic.close();
        symtab.close();
        littab.close();

        System.out.println("✅ Pass 1 completed: IC.txt, SYMTAB.txt, LITTAB.txt generated.");
    }

    // ------------------- PASS 2 -------------------
    static void pass2() throws IOException {
        Map<Integer, Integer> SYMTAB = loadTable("SYMTAB.txt");
        Map<Integer, Integer> LITTAB = loadTable("LITTAB.txt");

        BufferedReader ic = new BufferedReader(new FileReader("IC.txt"));
        BufferedWriter mc = new BufferedWriter(new FileWriter("MachineCode.txt"));

        String line;
        int lc = 0;

        while ((line = ic.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split("\\s+");

            // Handle START
            if (parts[0].equals("(AD,01)")) {
                lc = Integer.parseInt(parts[1].substring(3, parts[1].length() - 1));
                continue;
            }

            // Handle DC
            if (parts[0].equals("(DL,01)")) {
                String constant = parts[1].substring(3, parts[1].length() - 1);
                mc.write(lc + "\t00\t0\t" + constant + "\n");
                lc++;
                continue;
            }

            // Handle DS
            if (parts[0].equals("(DL,02)")) {
                lc++;
                continue;
            }

            // Handle IS
            if (parts[0].startsWith("(IS,")) {
                String opcode = parts[0].substring(4, parts[0].length() - 1);
                String reg = "0";
                String mem = "000";

                if (parts.length >= 2 && parts[1].matches("\\d"))
                    reg = parts[1];

                if (parts.length >= 3) {
                    String operand = parts[2];
                    if (operand.startsWith("(S,")) {
                        int index = Integer.parseInt(operand.substring(3, operand.length() - 1));
                        mem = String.format("%03d", SYMTAB.getOrDefault(index, 0));
                    } else if (operand.startsWith("(L,")) {
                        int index = Integer.parseInt(operand.substring(3, operand.length() - 1));
                        mem = String.format("%03d", LITTAB.getOrDefault(index, 0));
                    } else if (operand.startsWith("(C,")) {
                        mem = operand.substring(3, operand.length() - 1);
                    }
                }

                mc.write(lc + "\t" + opcode + "\t" + reg + "\t" + mem + "\n");
                lc++;
            }
        }

        ic.close();
        mc.close();
        System.out.println("✅ Pass 2 completed: MachineCode.txt generated.");
    }

    // ------------------- HELPER FUNCTIONS -------------------
    static String getRegisterCode(String reg) {
        switch (reg.toUpperCase()) {
            case "AREG": return "1";
            case "BREG": return "2";
            case "CREG": return "3";
            case "DREG": return "4";
            default: return "0";
        }
    }

    static Map<Integer, Integer> loadTable(String filename) throws IOException {
        Map<Integer, Integer> table = new LinkedHashMap<>();
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;
        int index = 1;
        while ((line = br.readLine()) != null) {
            String[] parts = line.trim().split("\\s+");
            if (parts.length >= 2)
                table.put(index++, Integer.parseInt(parts[1]));
        }
        br.close();
        return table;
    }
}
