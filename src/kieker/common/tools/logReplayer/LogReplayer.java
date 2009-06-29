package kieker.common.tools.logReplayer;

import kieker.tpmon.annotation.TpmonInternal;
import kieker.tpmon.core.TpmonController;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Andre van Hoorn
 */
public class LogReplayer {
    private static final Log log = LogFactory.getLog(LogReplayer.class);

    private static String inputDir = null;
    private static final TpmonController ctrl = TpmonController.getInstance();

    @TpmonInternal()
    public static void main(String[] args) {

        inputDir = System.getProperty("inputDir");
        if (inputDir == null || inputDir.length() == 0 || inputDir.equals("${inputDir}")) {
            log.error("No input dir found!");
            log.error("Provide an input dir as system property.");
            log.error("Example to read all tpmon-* files from /tmp:\n" +
                    "                    ant -DinputDir=/tmp/ run-reader    ");
            System.exit(1);
        } else {
            log.info("Reading all tpmon-* files from " + inputDir);
        }

        //instance.openAndRegisterData();
        log.info("Finished to read files");
        System.exit(0);
    }
}
