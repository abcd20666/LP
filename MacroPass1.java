import java.io.*;
import java.util.*;

public class MacroPass1 {
    public static void main(String[] args) throws IOException {
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

                // Read macro header
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

                // Write entry in MNT
                mnt.write(macroName + "\t" + pp + "\t" + kp + "\t" + mdtp + "\t" + (kp == 0 ? 0 : kpdtStart) + "\n");

                // Write parameters in PNTAB
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

                // Write MEND in MDT
                mdt.write("MEND\n");
                mdtp++;
                inMacro = false;
            } 
            else {
                // Write non-macro lines to intermediate file
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

        System.out.println("✅ Pass 1 of Macro Processor completed successfully!");
        System.out.println("✅ Generated files: mnt.txt, mdt.txt, kpdt.txt, pntab.txt, intermediate.txt");
    }
}
