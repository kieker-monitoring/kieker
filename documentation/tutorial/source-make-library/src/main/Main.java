package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

/**
 * The main class of the little program. The program itself takes a directory
 * containing libraries and a file containing descriptions and put both together
 * in a tex-file.
 * @author Nils Christian Ehmke
 * @version 1.0 The first implementation of the program.
 * @version 1.1 Some little modifications like for example line breaks.
 */
public class Main {

    private static final String COMMENT_START = "//";
    private static final String DESCR_SPLIT = "\t";
    private static final String OLD_WILD_CHAR = "$";
    private static final String NEW_WILD_CHAR = ".+";
    private static final String LINE_BREAK = System.getProperty("line.separator");
    private static final String TODO_MESSAGE = "n/a";

    /**
     * The program's main method.
     * @param args The command line arguments. The first argument should be the
     * file with the descriptions, the second argument should be the directory
     * with the libraries and the third argument should be the directory for the
     * destination file.
     * @throws Exception If something goes wrong.
     * @since 1.0
     */
    public static void main(String[] args) throws Exception {
        /* Make sure that we have at least three parameters. */
        if (args.length < 3) {
            System.err.println("Not enough parameters.");
            System.exit(1);
        }
        /* Get all the files and directories by name and make sure that they
        exist/can be created. */
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

        /* Open the writer for the destination file. */
        BufferedWriter writer = new BufferedWriter(new FileWriter(destFile));
        try {
            /* Create the frame of the table. */
            //writer.write("\\begin{figure}[H]" + LINE_BREAK);
            writer.write("\\begin{center}" + LINE_BREAK);
            writer.write("\\begin{longtable}{|p{0.4\\textwidth}|p{0.5\\textwidth}|}" + LINE_BREAK);
            writer.write("\\hline " + LINE_BREAK);
            writer.write("Filename & Description\\\\" + LINE_BREAK);
            writer.write("\\hline" + LINE_BREAK);
            writer.write("\\hline " + LINE_BREAK);

            /* For the first: Collect the descriptions. */
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
            /* Now get the files in the given library directory. */
            File libs[] = libDir.listFiles();
            System.out.println(libs.length + " files/directories found.");

            int ignored = 0;
            for (File lib : libs) {
                /* Ignore "invisible" files. */
                if (!lib.getName().startsWith(".")) {
                    System.out.print(lib.getName() + ": ");
                    boolean b = false;
                    for (int i = 0; !b && (i < descriptions.size()); i++) {
                        String name = descriptions.get(i).first;
                        b = lib.getName().matches(name);
                        if (b) {
                            writer.write(lib.getName() + " & " + descriptions.get(i).second + "\\\\" + LINE_BREAK);
                        }
                    }
                    if (b) {
                        System.out.println("description found.");
                    } else {
                        System.out.println("no description found.");
                        /* Write a todo */
                        writer.write(lib.getName() + " & " + TODO_MESSAGE + "\\\\" + LINE_BREAK);
                    }
                    writer.write("\\hline " + LINE_BREAK);
                } else {
                    ignored++;
                }
            }
            System.out.println(ignored + " files/directories ignored.");

            /* Now close the frame of the table */
            // writer.write("\\hline" + LINE_BREAK);
            writer.write("\\end{longtable}" + LINE_BREAK);
            writer.write("\\label{tabular:libraries}" + LINE_BREAK);
            //writer.write("\\caption{Libraries and files used by \\Kieker{}}" + LINE_BREAK);
            writer.write("\\end{center}" + LINE_BREAK);
            // writer.write("\\end{figure}" + LINE_BREAK);
        } finally {
            /* Don't forget to close the file. */
            writer.close();
        }
    }
}
