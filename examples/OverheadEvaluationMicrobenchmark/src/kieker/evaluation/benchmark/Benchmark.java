/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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

package kieker.evaluation.benchmark;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

import kieker.evaluation.monitoredApplication.MonitoredClass;

/**
 * @author Jan Waller
 */
public final class Benchmark {
	private static final String ENCODING = "UTF-8";

	private static PrintStream ps = null;
	private static String outputFn = null;
	private static int totalThreads = 0;
	private static int totalCalls = 0;
	private static long methodTime = 0;
	private static int recursionDepth = 0;

	private Benchmark() {}

	public static void main(final String[] args) throws InterruptedException {

		/* 1. Preparations */
		Benchmark.parseAndInitializeArguments(args);

		System.out.println(" # Experiment run configuration:"); // NOPMD (System.out)
		System.out.println(" # 1. Output filename " + Benchmark.outputFn); // NOPMD (System.out)
		System.out.println(" # 2. Recursion Depth " + Benchmark.recursionDepth); // NOPMD (System.out)
		System.out.println(" # 3. Threads " + Benchmark.totalThreads); // NOPMD (System.out)
		System.out.println(" # 4. Total-Calls " + Benchmark.totalCalls); // NOPMD (System.out)

		/* 2. Initialize Threads and Classes */
		final CountDownLatch doneSignal = new CountDownLatch(Benchmark.totalThreads);
		final MonitoredClass mc = new MonitoredClass();
		final BenchmarkingThread[] threads = new BenchmarkingThread[Benchmark.totalThreads];
		for (int i = 0; i < Benchmark.totalThreads; i++) {
			threads[i] = new BenchmarkingThread(mc, Benchmark.totalCalls, Benchmark.methodTime, Benchmark.recursionDepth, doneSignal);
		}
		for (int l = 0; l < 4; l++) {
			{ // NOCS (reserve mem only within the block)
				final long freeMemChunks = Runtime.getRuntime().freeMemory() >> 27;
				// System.out.println("Free-Mem: " + Runtime.getRuntime().freeMemory());
				final int memSize = 128 * 1024 * 128; // memSize * 8 = total Bytes -> 128MB
				for (int j = 0; j < freeMemChunks; j++) {
					final long[] grabMemory = new long[memSize];
					for (int i = 0; i < memSize; i++) {
						grabMemory[i] = System.nanoTime();
					}
				}
				// System.out.println("done grabbing memory...");
				// System.out.println("Free-Mem: " + Runtime.getRuntime().freeMemory());
			}
			Thread.sleep(5000);
		}
		/* 3. Starting Threads */
		for (int i = 0; i < Benchmark.totalThreads; i++) {
			threads[i].start();
		}

		/* 4. Wait for all Threads */
		try {
			doneSignal.await();
		} catch (final InterruptedException e) {
			e.printStackTrace(); // NOPMD (Stacktrace)
			System.exit(-1);
		}

		/* 5. Print experiment statistics */
		System.out.print(" # 5. Writing results ... "); // NOPMD (System.out)
		// CSV Format: configuration;order_index;Thread-ID;duration_nsec
		long[] timings;
		for (int h = 0; h < Benchmark.totalThreads; h++) {
			timings = threads[h].getTimings();
			for (int i = 0; i < Benchmark.totalCalls; i++) {
				Benchmark.ps.println(threads[h].getName() + ";" + timings[i]);
			}
		}
		Benchmark.ps.close();

		System.out.println("done"); // NOPMD (System.out)
		System.out.println(" # "); // NOPMD (System.out)

		System.exit(0);
	}

	@SuppressWarnings("static-access")
	public static void parseAndInitializeArguments(final String[] args) {
		final Options cmdlOpts = new Options();
		cmdlOpts.addOption(OptionBuilder.withLongOpt("totalcalls").withArgName("calls").hasArg(true).isRequired(true)
				.withDescription("Number of total Method-Calls performed.").withValueSeparator('=').create("t"));
		cmdlOpts.addOption(OptionBuilder.withLongOpt("methodtime").withArgName("time").hasArg(true).isRequired(true).withDescription("Time a method call takes.")
				.withValueSeparator('=').create("m"));
		cmdlOpts.addOption(OptionBuilder.withLongOpt("totalthreads").withArgName("threads").hasArg(true).isRequired(true)
				.withDescription("Number of Threads started.").withValueSeparator('=').create("h"));
		cmdlOpts.addOption(OptionBuilder.withLongOpt("recursiondepth").withArgName("depth").hasArg(true).isRequired(true)
				.withDescription("Depth of Recursion performed.").withValueSeparator('=').create("d"));
		cmdlOpts.addOption(OptionBuilder.withLongOpt("output-filename").withArgName("filename").hasArg(true).isRequired(true)
				.withDescription("Filename of results file. Output is appended if file exists.").withValueSeparator('=').create("o"));
		try {
			CommandLine cmdl = null;
			final CommandLineParser cmdlParser = new BasicParser();
			cmdl = cmdlParser.parse(cmdlOpts, args);
			Benchmark.outputFn = cmdl.getOptionValue("output-filename");
			Benchmark.totalCalls = Integer.parseInt(cmdl.getOptionValue("totalcalls"));
			Benchmark.methodTime = Integer.parseInt(cmdl.getOptionValue("methodtime"));
			Benchmark.totalThreads = Integer.parseInt(cmdl.getOptionValue("totalthreads"));
			Benchmark.recursionDepth = Integer.parseInt(cmdl.getOptionValue("recursiondepth"));
			Benchmark.ps = new PrintStream(new FileOutputStream(Benchmark.outputFn, true), false, Benchmark.ENCODING);
		} catch (final Exception ex) { // NOCS (e.g., IOException, ParseException, NumberFormatException)
			new HelpFormatter().printHelp(Benchmark.class.getName(), cmdlOpts);
			ex.printStackTrace(); // NOPMD (Stacktrace)
			System.exit(-1);
		}
	}
}
