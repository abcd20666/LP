import java.io.*;
import java.util.*;

public class MacroProcessor {

    public static void main(String[] args) throws IOException {
        pass1();
        pass2();
        System.out.println("\n✅ Macro Processor (Pass 1 + Pass 2) completed successfully!");
    }

    // ---------------- PASS 1 -----------------
    public static void pass1() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("macro_input.txt"));
        BufferedWriter mnt = new BufferedWriter(new FileWriter("mnt.txt"));
        BufferedWriter mdt = new BufferedWriter(new FileWriter("mdt.txt"));
        BufferedWriter kpdt = new BufferedWriter(new FileWriter("kpdt.txt"));
        BufferedWriter pntab = new BufferedWriter(new FileWriter("pntab.txt"));
        BufferedWriter intermediate = new BufferedWriter(new FileWriter("intermediate.txt"));

        String line;
        boolean inMacro = false;
        int mdtp = 1, kpdtp = 1;
        List<String> pntabList = new ArrayList<>();

        while ((line = br.readLine()) != null) {
            line = line.trim();

            if (line.equalsIgnoreCase("MACRO")) {
                inMacro = true;
                String header = br.readLine().trim();
                String[] parts = header.split("[ ,]+");
                String macroName = parts[0];

                pntabList.clear();
                int pp = 0, kp = 0;
                int kpdtStart = kpdtp;

                // Process parameters
                for (int i = 1; i < parts.length; i++) {
                    String param = parts[i].replace("&", "");
                    if (param.contains("=")) {
                        String[] kv = param.split("=");
                        String key = kv[0];
                        String value = (kv.length > 1) ? kv[1] : "-";
                        kpdt.write(key + "\t" + value + "\n");
                        pntabList.add(key);
                        kp++;
                        kpdtp++;
                    } else {
                        pntabList.add(param);
                        pp++;
                    }
                }

                // Write to MNT
                mnt.write(macroName + "\t" + pp + "\t" + kp + "\t" + mdtp + "\t" + (kp == 0 ? 0 : kpdtStart) + "\n");

                // Write to PNTAB
                for (String p : pntabList) {
                    pntab.write(macroName + " -> " + p + "\n");
                }

                // Process macro body
                while (!(line = br.readLine().trim()).equalsIgnoreCase("MEND")) {
                    String[] bodyParts = line.split("[ ,]+");
                    StringBuilder sb = new StringBuilder(bodyParts[0]);

                    for (int i = 1; i < bodyParts.length; i++) {
                        String op = bodyParts[i];
                        if (op.startsWith("&")) {
                            String param = op.replace("&", "");
                            int index = pntabList.indexOf(param) + 1;
                            sb.append("\t(P,").append(index).append(")");
                        } else {
                            sb.append("\t").append(op);
                        }
                    }

                    mdt.write(sb.toString() + "\n");
                    mdtp++;
                }

                // Write MEND
                mdt.write("MEND\n");
                mdtp++;
                inMacro = false;
            } else {
                if (!line.isEmpty()) {
                    intermediate.write(line + "\n");
                }
            }
        }

        br.close();
        mnt.close();
        mdt.close();
        kpdt.close();
        pntab.close();
        intermediate.close();

        System.out.println("✅ Pass 1 completed: mnt.txt, mdt.txt, kpdt.txt, pntab.txt, intermediate.txt generated.");
    }

    // ---------------- PASS 2 -----------------
    public static void pass2() throws IOException {
        BufferedReader mnt = new BufferedReader(new FileReader("mnt.txt"));
        BufferedReader mdt = new BufferedReader(new FileReader("mdt.txt"));
        BufferedReader kpdt = new BufferedReader(new FileReader("kpdt.txt"));
        BufferedReader intermediate = new BufferedReader(new FileReader("intermediate.txt"));
        BufferedWriter output = new BufferedWriter(new FileWriter("output.txt"));

        // Load MNT
        Map<String, String[]> mntTable = new HashMap<>();
        String line;
        while ((line = mnt.readLine()) != null) {
            String[] parts = line.split("\\s+");
            mntTable.put(parts[0], parts); // macroName -> [name, pp, kp, mdtp, kpdtp]
        }

        // Load MDT
        List<String> mdtList = new ArrayList<>();
        while ((line = mdt.readLine()) != null) {
            mdtList.add(line);
        }

        // Load KPDT
        List<String[]> kpdtList = new ArrayList<>();
        while ((line = kpdt.readLine()) != null) {
            kpdtList.add(line.split("\\s+"));
        }

        // Expand macros
        while ((line = intermediate.readLine()) != null) {
            String[] parts = line.trim().split("[ ,]+");
            String name = parts[0];

            if (mntTable.containsKey(name)) {
                String[] entry = mntTable.get(name);
                int mdtp = Integer.parseInt(entry[3]);
                int kp = Integer.parseInt(entry[2]);
                int kpdtp = Integer.parseInt(entry[4]);

                // Parameter map (for substitution)
                Map<Integer, String> params = new HashMap<>();
                int paramIndex = 1;

                // Default parameters from KPDT
                for (int i = 0; i < kp; i++) {
                    String[] kv = kpdtList.get(kpdtp - 1 + i);
                    params.put(paramIndex++, kv.length > 1 ? kv[1] : "-");
                }

                // Override with actual parameters
                for (int i = 1; i < parts.length; i++) {
                    String[] kv = parts[i].split("=");
                    if (kv.length == 2)
                        params.put(i, kv[1]);
                    else
                        params.put(i, kv[0]);
                }

                // Expand MDT
                for (int i = mdtp - 1; i < mdtList.size(); i++) {
                    String body = mdtList.get(i);
                    if (body.equalsIgnoreCase("MEND")) break;

                    for (Map.Entry<Integer, String> e : params.entrySet()) {
                        body = body.replace("(P," + e.getKey() + ")", e.getValue());
                    }

                    output.write(body + "\n");
                }
            } else {
                output.write(line + "\n");
            }
        }

        mnt.close();
        mdt.close();
        kpdt.close();
        intermediate.close();
        output.close();

        System.out.println("✅ Pass 2 completed: output.txt (expanded source) generated.");
    }
}
