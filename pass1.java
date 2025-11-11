import java.io.*;
import java.util.*;

public class pass1 {
    static Map<String, String> is = new HashMap<>();
    static Map<String, String> dl = new HashMap<>();
    static Map<String, String> ad = new HashMap<>();
    static Map<String, String> regTable = new HashMap<>();
    static Map<String, Integer> symbolTable = new LinkedHashMap<>();
    static Map<String, Integer> literalTable = new LinkedHashMap<>();
    static List<String[]> intermediateCode = new ArrayList<>();

    static int locCounter = 0;

    public static void main(String[] args) throws IOException {
        initializeTables();
        passOne("Input.txt");
        writeIC("IC.txt");
        writeTable("SYMTAB.txt", symbolTable);
        writeTable("LITTAB.txt", literalTable);

        System.out.println("✅ PASS 1 Completed Successfully!");
        System.out.println("Generated Files: IC.txt, SYMTAB.txt, LITTAB.txt");
    }

    static void initializeTables() {
        // Imperative Statements (IS)
        is.put("STOP", "00");
        is.put("ADD", "01");
        is.put("SUB", "02");
        is.put("MULT", "03");
        is.put("MOVER", "04");
        is.put("MOVEM", "05");
        is.put("COMP", "06");
        is.put("BC", "07");
        is.put("DIV", "08");
        is.put("READ", "09");
        is.put("PRINT", "10");

        // Declarative Statements (DL)
        dl.put("DC", "01");
        dl.put("DS", "02");

        // Assembler Directives (AD)
        ad.put("START", "01");
        ad.put("END", "02");
        ad.put("ORIGIN", "03");
        ad.put("EQU", "04");
        ad.put("LTORG", "05");

        // Register Table
        regTable.put("AREG", "1");
        regTable.put("BREG", "2");
        regTable.put("CREG", "3");
        regTable.put("DREG", "4");
    }

    static void passOne(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;

        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split("[\\s,]+");
            String label = null, opcode, op1 = null, op2 = null;

            if (is.containsKey(parts[0]) || dl.containsKey(parts[0]) || ad.containsKey(parts[0])) {
                opcode = parts[0];
                if (parts.length > 1) op1 = parts[1];
                if (parts.length > 2) op2 = parts[2];
            } else {
                label = parts[0];
                opcode = parts[1];
                if (parts.length > 2) op1 = parts[2];
                if (parts.length > 3) op2 = parts[3];
                symbolTable.put(label, locCounter);
            }

            // START
            if (opcode.equals("START")) {
                locCounter = Integer.parseInt(op1);
                intermediateCode.add(new String[]{"(AD,01)", "(C," + op1 + ")"});
                continue;
            }

            // END or LTORG
            if (opcode.equals("END") || opcode.equals("LTORG")) {
                for (Map.Entry<String, Integer> entry : literalTable.entrySet()) {
                    if (entry.getValue() == -1) {
                        literalTable.put(entry.getKey(), locCounter++);
                        intermediateCode.add(new String[]{"(DL,01)", "(C," + entry.getKey().replaceAll("[='']", "") + ")"});
                    }
                }
                intermediateCode.add(new String[]{"(AD," + ad.get(opcode) + ")"});
                continue;
            }

            // ORIGIN
            if (opcode.equals("ORIGIN")) {
                locCounter = evaluateExpression(op1);
                intermediateCode.add(new String[]{"(AD,03)", "(C," + op1 + ")"});
                continue;
            }

            // EQU
            if (opcode.equals("EQU")) {
                int val = evaluateExpression(op1);
                symbolTable.put(label, val);
                intermediateCode.add(new String[]{"(AD,04)", "(C," + val + ")"});
                continue;
            }

            // Declarative Statements (DC, DS)
            if (dl.containsKey(opcode)) {
                if (opcode.equals("DC")) {
                    intermediateCode.add(new String[]{"(DL,01)", "(C," + op1 + ")"});
                    locCounter++;
                } else if (opcode.equals("DS")) {
                    intermediateCode.add(new String[]{"(DL,02)", "(C," + op1 + ")"});
                    locCounter += Integer.parseInt(op1);
                }
                continue;
            }

            // Imperative Statements
            if (is.containsKey(opcode)) {
                List<String> ic = new ArrayList<>();
                ic.add("(IS," + is.get(opcode) + ")");

                if (op1 != null) {
                    if (regTable.containsKey(op1)) {
                        ic.add("(R," + regTable.get(op1) + ")");
                    } else if (op1.startsWith("='")) {
                        literalTable.putIfAbsent(op1, -1);
                        ic.add("(L," + getLiteralIndex(op1) + ")");
                    } else {
                        symbolTable.putIfAbsent(op1, -1);
                        ic.add("(S," + getSymbolIndex(op1) + ")");
                    }
                }

                if (op2 != null) {
                    if (op2.startsWith("='")) {
                        literalTable.putIfAbsent(op2, -1);
                        ic.add("(L," + getLiteralIndex(op2) + ")");
                    } else {
                        symbolTable.putIfAbsent(op2, -1);
                        ic.add("(S," + getSymbolIndex(op2) + ")");
                    }
                }

                locCounter++;
                intermediateCode.add(ic.toArray(new String[0]));
            }
        }
        br.close();
    }

    static int evaluateExpression(String expr) {
        // Example: LOOP+2, X-1
        if (expr.contains("+")) {
            String[] parts = expr.split("\\+");
            return symbolTable.getOrDefault(parts[0], 0) + Integer.parseInt(parts[1]);
        } else if (expr.contains("-")) {
            String[] parts = expr.split("\\-");
            return symbolTable.getOrDefault(parts[0], 0) - Integer.parseInt(parts[1]);
        } else {
            try {
                return Integer.parseInt(expr);
            } catch (NumberFormatException e) {
                return symbolTable.getOrDefault(expr, 0);
            }
        }
    }

    static int getSymbolIndex(String symbol) {
        int index = 1;
        for (String sym : symbolTable.keySet()) {
            if (sym.equals(symbol)) return index;
            index++;
        }
        return index;
    }

    static int getLiteralIndex(String lit) {
        int index = 1;
        for (String l : literalTable.keySet()) {
            if (l.equals(lit)) return index;
            index++;
        }
        return index;
    }

    static void writeIC(String filename) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
        for (String[] line : intermediateCode) {
            for (String word : line) {
                bw.write(word + "\t");
            }
            bw.newLine();
        }
        bw.close();
    }

    static void writeTable(String filename, Map<String, Integer> table) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
        for (Map.Entry<String, Integer> e : table.entrySet()) {
            bw.write(e.getKey() + "\t" + e.getValue() + "\n");
        }
        bw.close();
    }
}
