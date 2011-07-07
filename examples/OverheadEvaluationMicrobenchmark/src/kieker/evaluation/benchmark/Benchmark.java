package kieker.evaluation.benchmark;

import kieker.evaluation.monitoredApplication.MonitoredClass;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

/**
 * @author Jan Waller
 */
public final class Benchmark {

	private static PrintStream ps = null;
	private static String configurationId = null;
	private static String outputFn = null;
	private static int totalThreads = 0;
	private static int totalCalls = 0;
	private static long methodTime = 0;
	private static int recursionDepth = 0;

	public static void main(String[] args) {

		/* 1. Preparations */
		parseAndInitializeArguments(args);

		System.out.println(" # Experiment run configuration:");
		System.out.println(" # 1. Output filename " + outputFn);
		System.out.println(" # 2. Recursion Depth " + recursionDepth);
		System.out.println(" # 3. Threads " + totalThreads);
		System.out.println(" # 4. Total-Calls " + totalCalls);

		/* 2. Initialize Threads and Classes */
		CountDownLatch doneSignal = new CountDownLatch(totalThreads);
		MonitoredClass mc = new MonitoredClass();
		BenchmarkingThread[] threads = new BenchmarkingThread[totalThreads];
		for (int i = 0; i < totalThreads; i++) {
			threads[i] = new BenchmarkingThread(mc, totalCalls, methodTime, recursionDepth, doneSignal);
		}
		{// hopefully this works ;)
			final long freeMemChunks = Runtime.getRuntime().freeMemory() >> 27;
			//System.out.println("Free-Mem: " + Runtime.getRuntime().freeMemory());
			final int memSize = 128 * 1024 * 128; // memSize * 8 = total Bytes -> 128MB
			for (int j = 0; j < freeMemChunks; j++) {
				final long[] grabMemory = new long[memSize];
				for (int i = 0; i < memSize; i++) {
					grabMemory[i] = System.nanoTime();
				}
			}
			//System.out.println("done grabbing memory...");
			//System.out.println("Free-Mem: " + Runtime.getRuntime().freeMemory());
		}

		/* 3. Starting Threads */
		for (int i = 0; i < totalThreads; i++) {
			threads[i].start();
		}

		/* 4. Wait for all Threads */
		try {
			doneSignal.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(-1);
		}

		/* 5. Print experiment statistics */
		System.out.print(" # 5. Writing results ... ");
		// CSV Format: configuration;order_index;Thread-ID;duration_nsec
		long[] timings;
		for (int h = 0; h < totalThreads; h++) {
			timings = threads[h].getTimings();
			for (int i = 0; i < totalCalls; i++) {
				ps.println(configurationId + ";" + threads[h].getName() + ";" + timings[i]);
			}
		}
		ps.close();

		System.out.println("done");
		System.out.println(" # ");

		System.exit(0);
	}

	@SuppressWarnings("static-access")
	public static void parseAndInitializeArguments(String[] args) {
		final Options cmdlOpts = new Options();
		cmdlOpts.addOption(OptionBuilder.withLongOpt("configuration-id").withArgName("identifier").hasArg(true).isRequired(true)
				.withDescription("Each line written to the CSV results file will start with this identifier.").withValueSeparator('=').create("c"));
		cmdlOpts.addOption(OptionBuilder.withLongOpt("totalcalls").withArgName("calls").hasArg(true).isRequired(true)
				.withDescription("Number of total Method-Calls performed.").withValueSeparator('=').create("t"));
		cmdlOpts.addOption(OptionBuilder.withLongOpt("methodtime").withArgName("time").hasArg(true).isRequired(true)
				.withDescription("Time a method call takes.").withValueSeparator('=').create("m"));
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
			outputFn = cmdl.getOptionValue("output-filename");
			configurationId = cmdl.getOptionValue("configuration-id");
			totalCalls = Integer.parseInt(cmdl.getOptionValue("totalcalls"));
			methodTime = Integer.parseInt(cmdl.getOptionValue("methodtime"));
			totalThreads = Integer.parseInt(cmdl.getOptionValue("totalthreads"));
			recursionDepth = Integer.parseInt(cmdl.getOptionValue("recursiondepth"));
			ps = new PrintStream(new FileOutputStream(outputFn, true));
		} catch (Exception ex) {
			new HelpFormatter().printHelp(Benchmark.class.getName(), cmdlOpts);
			ex.printStackTrace();
			System.exit(-1);
		}
	}
}
