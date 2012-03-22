/***************************************************************************
 * Copyright 2011 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/

package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
 * The main class of the little program. The program itself takes a directory
 * containing libraries and a file containing descriptions and put both together
 * in a tex-file.
 * 
 * @author Nils Christian Ehmke
 * 
 * @version 1.0 The first implementation of the program.
 * @version 1.1 Some little modifications like for example line breaks.
 * @version 1.2 Made sure that only JAR files will be used (no other files and
 *          no directories). Made also some structural improvement.
 */
public class Main {

	private static final String COMMENT_START = "//";
	private static final String DESCR_SPLIT = "\t";
	private static final String OLD_WILD_CHAR = "$";
	private static final String NEW_WILD_CHAR = ".+";
	private static final String LINE_BREAK = System
			.getProperty("line.separator");
	private static final String TODO_MESSAGE = "n/a";
	/* The only allowed extension. This must be in upper case. */
	private static final String ALLOWED_EXT = "JAR";

	private File descrFile;
	private File libDir;
	private File destFile;
	private ArrayList<Pair<String, String>> descriptions;

	/**
	 * Execute the single tasks of the program.
	 * 
	 * @param args
	 *            The command line arguments from the main method.
	 * @throws IOException
	 *             If something went wrong.
	 * @since 1.2
	 */
	private void doJob(String[] args) throws IOException {
		checkAndCreateFiles(args);
		getMapping();
		writeContent();
	}

	/**
	 * Writes the content to the tex-file.
	 * 
	 * @throws IOException
	 *             If an error occurred.
	 * @since 1.2
	 */
	private void writeContent() throws IOException {
		/* Open the writer for the destination file. */
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(destFile));
			/* Create the frame of the table. */
			writer.write("\\begin{center}" + LINE_BREAK);
			writer
					.write("\\begin{longtable}{|p{0.4\\textwidth}|p{0.5\\textwidth}|}"
							+ LINE_BREAK);
			writer.write("\\hline " + LINE_BREAK);
			writer.write("Filename & Description\\\\" + LINE_BREAK);
			writer.write("\\hline" + LINE_BREAK);
			writer.write("\\hline " + LINE_BREAK);

			System.out.println(descriptions.size() + " descriptions found.");
			/* Now get the files in the given library directory. */
			File libs[] = libDir.listFiles(new FilenameFilter() {

				
				public boolean accept(File dir, String name) {
					// * Only JAR files. */
					return name.toUpperCase().endsWith(ALLOWED_EXT);
				}

			});
                        System.out.println("Sorting libs ...");
                        Arrays.sort(libs, new FileComparator()); // sort alphabetically
			System.out.println(libs.length + " files/directories found.");
			for (File lib : libs) {
					System.out.print(lib.getName() + ": ");
					boolean b = false;
					for (int i = 0; !b && (i < descriptions.size()); i++) {
						String name = descriptions.get(i).first;
						b = lib.getName().matches(name);
						if (b) {
							writer.write(lib.getName() + " & "
									+ descriptions.get(i).second + "\\\\"
									+ LINE_BREAK);
						}
					}
					if (b) {
						System.out.println("description found.");
					} else {
						System.out.println("no description found.");
						/* Write a n/a */
						writer.write(lib.getName() + " & " + TODO_MESSAGE
								+ "\\\\" + LINE_BREAK);
					}
					writer.write("\\hline " + LINE_BREAK);
			}

			/* Now close the frame of the table */
			writer.write("\\end{longtable}" + LINE_BREAK);
			writer.write("\\label{tabular:libraries}" + LINE_BREAK);
			writer.write("\\end{center}" + LINE_BREAK);
		} finally {
			/* Don't forget to close the file. */
			if (writer != null) {
				writer.close();
			}
		}
	}

	/**
	 * Gets the mapping from the libraries to the descriptions.
	 * 
	 * @throws IOException
	 *             If an error occurred.
	 * @since 1.2
	 */
	private void getMapping() throws IOException {
		/* Collect the descriptions. */
		descriptions = new ArrayList<Pair<String, String>>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(descrFile));
			String line;
			while ((line = reader.readLine()) != null) {
				if (!line.startsWith(COMMENT_START)) {
					line = line.replace(OLD_WILD_CHAR, NEW_WILD_CHAR);
					String split[] = line.split(DESCR_SPLIT);
					if (split.length == 2) {
						descriptions.add(new Pair<String, String>(split[0],
								split[1]));
					}
				}
			}
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}

	private void checkAndCreateFiles(String[] args) throws IOException {
		/* Make sure that we have at least three parameters. */
		if (args.length < 3) {
			System.err.println("Not enough parameters.");
			System.exit(1);
		}
		/*
		 * Get all the files and directories by name and make sure that they
		 * exist/can be created.
		 */
		descrFile = new File(args[0]);
		libDir = new File(args[1]);
		destFile = new File(args[2]);
		if (!(descrFile.isFile() && libDir.isDirectory())) {
			System.err
					.println("One or more of the given files does not exist.");
			System.exit(1);
		}
		destFile.delete();
		if (!destFile.createNewFile()) {
			System.err.println("The destination file could not be created.");
			System.exit(1);
		}
	}

	/**
	 * The program's main method.
	 * 
	 * @param args
	 *            The command line arguments. The first argument should be the
	 *            file with the descriptions, the second argument should be the
	 *            directory with the libraries and the third argument should be
	 *            the directory for the destination file.
	 * @throws Exception
	 *             If something goes wrong.
	 * @since 1.0
	 */
	public static void main(String[] args) throws Exception {
		new Main().doJob(args);
	}

    /** source: http://weblog.janek.org/Archive/2005/01/16/HowtoSortFilesandDirector.html */
    private static class FileComparator
            implements Comparator<File> {

        private Collator c = Collator.getInstance();

        public int compare(File f1,
                File f2) {
            if (f1 == f2) {
                return 0;
            }

            if (f1.isDirectory() && f2.isFile()) {
                return -1;
            }
            if (f1.isFile() && f2.isDirectory()) {
                return 1;
            }

            return c.compare(f1.getName(), f2.getName());
        }
    }
}
