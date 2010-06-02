package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

/**
 * @author nils
 */
public class Main {

    private static final String COMMENT_START = "//";
    private static final String DESCR_SPLIT = "\t";
    private static final String OLD_WILD_CHAR = "$";
    private static final String NEW_WILD_CHAR = ".+";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        if (args.length < 3) {
            System.err.println("Not enough parameters.");
            System.exit(1);
        }
        File descrFile = new File(args[0]);
        File libDir = new File(args[1]);
        File destFile = new File(args[2]);
        if (!(descrFile.isFile() && libDir.isDirectory())) {
            System.err.println("One or more of the given files does not exist.");
            System.exit(1);
        }
        destFile.delete();
        if (!destFile.createNewFile()) {
            System.err.println("The destination file could not be created.");
            System.exit(1);
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(destFile));
        writer.write("\\begin{figure}[H]"+
	"\\begin{center}"+
		"\\begin{tabular}{|c|c|}"+
			"\\hline "+
			"Filename & Description\\\\" +
			"\\hline"+
			"\\hline ");
        ArrayList<Pair<String, String>> descriptions = new ArrayList<Pair<String, String>>();
        BufferedReader reader = new BufferedReader(new FileReader(descrFile));
        String line;
        while ((line = reader.readLine()) != null) {
            if (!line.startsWith(COMMENT_START)) {
                line = line.replace(OLD_WILD_CHAR, NEW_WILD_CHAR);
                String split[] = line.split(DESCR_SPLIT);
                if (split.length == 2) {
                    descriptions.add(new Pair<String, String>(split[0], split[1]));
                }
            }
        }
        System.out.println(descriptions.size() + " descriptions found.");
        File libs[] = libDir.listFiles();
        System.out.println(libs.length + " files found.");
        for (File lib : libs) {
            System.out.print(lib.getName() + ": ");
            boolean b = false;
            for (int i = 0; !b && (i < descriptions.size()); i++) {
                String name = descriptions.get(i).first;
                b = lib.getName().matches(name);
                if (b) {
                 writer.write(lib.getName() + " & " + descriptions.get(i).second + "\\\\");
                }
            }
            if (b) {
                System.out.println("description found.");
            } else {
                System.out.println("no description found.");
            }

        }

        writer.write("\\hline"+
		"\\end{tabular}"+
		"\\label{tabular:libraries}"+
		"\\caption{Libraries and files used by \\Kieker{}}"+
	"\\end{center}"+
"\\end{figure}");
        writer.close();
    }
}
