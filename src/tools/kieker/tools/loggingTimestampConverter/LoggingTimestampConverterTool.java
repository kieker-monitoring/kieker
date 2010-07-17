package kieker.tools.loggingTimestampConverter;

/*
 * ==================LICENCE=========================
 * Copyright 2006-2010 Kieker Project
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
 * ==================================================
 */

import java.util.Comparator;
import java.util.Vector;
import kieker.common.util.LoggingTimestampConverter;
import kieker.tools.traceAnalysis.TraceAnalysisTool;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Andre van Hoorn
 */
public class LoggingTimestampConverterTool {

    private static final Log log = LogFactory.getLog(LoggingTimestampConverterTool.class);
    private static CommandLine cmdl = null;
    private static final CommandLineParser cmdlParser = new BasicParser();
    private static final HelpFormatter cmdHelpFormatter = new HelpFormatter();
    private static final Options cmdlOpts = new Options();
    private static final Vector<Option> options = new Vector<Option>();
    private static String[] timestampsStr;
    private static long[] timestampsLong;
    private static final String CMD_OPT_NAME__TIMESTAMPS = "timestamps";

    static {
        // TODO: OptionGroups?
        options.add(OptionBuilder.withLongOpt(CMD_OPT_NAME__TIMESTAMPS).withArgName("timestamp1 ... timestampN").hasArgs().isRequired(true).withDescription("List of timestamps (UTC timezone) to convert").create("t"));

        for (Option o : options) {
            cmdlOpts.addOption(o);
        }
        cmdHelpFormatter.setOptionComparator(new Comparator() {

            public int compare(Object o1, Object o2) {
                if (o1 == o2) {
                    return 0;
                }
                int posO1 = options.indexOf(o1);
                int posO2 = options.indexOf(o2);
                if (posO1 < posO2) {
                    return -1;
                }
                if (posO1 > posO2) {
                    return 1;
                }
                return 0;
            }
        });
    }

    public static void main(String[] args) {
        if (!parseArgs(args) || !initFromArgs()) {
            System.exit(1);
        }

        for (long tstamp : timestampsLong) {
            StringBuilder strB = new StringBuilder();
            strB.append(tstamp).append(": ")
                    .append(LoggingTimestampConverter.convertLoggingTimestampToUTCString(tstamp))
                    .append(" (").append(LoggingTimestampConverter.convertLoggingTimestampLocalTimeZoneString(tstamp))
                    .append(")");
            System.out.println(strB.toString());
        }
    }

    private static boolean parseArgs(String[] args) {
        try {
            cmdl = cmdlParser.parse(cmdlOpts, args);
        } catch (ParseException e) {
            printUsage();
            System.err.println("\nError parsing arguments: " + e.getMessage());
            return false;
        }
        return true;
    }

    private static void printUsage() {
        cmdHelpFormatter.printHelp(80, TraceAnalysisTool.class.getName(), "", cmdlOpts, "", true);
    }

    private static boolean initFromArgs() {
        timestampsStr = cmdl.getOptionValues(CMD_OPT_NAME__TIMESTAMPS);
        if (timestampsStr == null) { // should not happen since marked as required opt
            log.error("Missing value for option '" + CMD_OPT_NAME__TIMESTAMPS + "'");
            return false;
        }

        timestampsLong = new long[timestampsStr.length];
        for (int curIdx = 0; curIdx<timestampsStr.length; curIdx++) {
            try {
                timestampsLong[curIdx] = Long.parseLong(timestampsStr[curIdx]);
                curIdx++;
            } catch (NumberFormatException ex) {
                log.error("Failed to parse timestamp:" + timestampsStr[curIdx], ex);
                System.err.println("Failed to parse timestamp:" + timestampsStr[curIdx]);
                return false;
            }
        }

        return true;
    }
}
