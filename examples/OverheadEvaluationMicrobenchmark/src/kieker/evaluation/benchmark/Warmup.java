package kieker.evaluation.benchmark;

import kieker.evaluation.monitoredApplication.MonitoredClass;
import java.io.FileOutputStream;
import java.io.PrintStream;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

/**
 * @author Jan Waller
 */
public class Warmup {

    private static PrintStream ps = null;
    private static String configurationId = null;
    private static String outputFn = null;
    private static int totalCalls = 0;
    private static int recordedCalls = 0;
    private static long methodTime = 0;

    public static void main(String[] args) {

        /* 1. Preparations */
        parseAndInitializeArguments(args);

        System.out.println(" # Experiment run configuration:");
        System.out.println(" # 1. Output filename " + outputFn);
        System.out.println(" # 2. Total-Calls " + totalCalls);
        System.out.println(" # 3. Recorded-Calls " + recordedCalls);

        long[] timings = new long[recordedCalls];
        MonitoredClass mc = new MonitoredClass();
        long start_ns, stop_ns;

        /* 2. Execute recording phase */
        int j = 0;
        for (int i = 0; i < totalCalls; i++) {
            start_ns = System.nanoTime();
            mc.monitoredMethod(methodTime);
            stop_ns = System.nanoTime();
            timings[j] = stop_ns - start_ns;
            j = (j + 1) % recordedCalls;
        }

        /* 3. Print experiment statistics */
        System.out.print(" # 4. Writing results ... ");
        // CSV Format: configuration;order_index;duration_nsec
        for (int i = 0; i < recordedCalls; i++) {
            ps.println(configurationId + ";" + timings[(j + i) % recordedCalls]);
        }
        ps.close();

        System.out.println("done");
        System.out.println(" # ");

        System.exit(0);
    }

    @SuppressWarnings("static-access")
    public static void parseAndInitializeArguments(String[] args) {
        final Options cmdlOpts = new Options();
        cmdlOpts.addOption(OptionBuilder.withLongOpt("configuration-id").withArgName("identifier").hasArg(true).isRequired(true).withDescription("Each line written to the CSV results file will start with this identifier.").withValueSeparator('=').create("c"));
        cmdlOpts.addOption(OptionBuilder.withLongOpt("recordedcalls").withArgName("calls").hasArg(true).isRequired(true).withDescription("Number of recorded Method-Calls performed.").withValueSeparator('=').create("r"));
        cmdlOpts.addOption(OptionBuilder.withLongOpt("totalcalls").withArgName("calls").hasArg(true).isRequired(true).withDescription("Number of total Method-Calls performed.").withValueSeparator('=').create("t"));
        cmdlOpts.addOption(OptionBuilder.withLongOpt("methodtime").withArgName("time").hasArg(true).isRequired(true).withDescription("Time a method call takes.").withValueSeparator('=').create("m"));
        cmdlOpts.addOption(OptionBuilder.withLongOpt("output-filename").withArgName("filename").hasArg(true).isRequired(true).withDescription("Filename of results file. Output is appended if file exists.").withValueSeparator('=').create("o"));
        try {
            CommandLine cmdl = null;
            final CommandLineParser cmdlParser = new BasicParser();
            cmdl = cmdlParser.parse(cmdlOpts, args);
            outputFn = cmdl.getOptionValue("output-filename");
            configurationId = cmdl.getOptionValue("configuration-id");
            recordedCalls = Integer.parseInt(cmdl.getOptionValue("recordedcalls"));
            totalCalls = Integer.parseInt(cmdl.getOptionValue("totalcalls"));
            methodTime = Integer.parseInt(cmdl.getOptionValue("methodtime"));
            ps = new PrintStream(new FileOutputStream(outputFn, true));
        } catch (Exception ex) {
            new HelpFormatter().printHelp(Warmup.class.getName(), cmdlOpts);
            ex.printStackTrace();
            System.exit(-1);
        }
    }
}
