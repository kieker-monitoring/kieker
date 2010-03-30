package kieker.tools.logReplayer;

import java.util.StringTokenizer;
import kieker.tpan.reader.AbstractKiekerMonitoringLogReader;
import kieker.tpan.consumer.IKiekerRecordConsumer;
import kieker.tpan.reader.LogReaderExecutionException;
import kieker.tpan.consumer.RecordConsumerExecutionException;

import kieker.tools.logReplayer.RealtimeReplayDistributor;
import kieker.tpan.reader.filesystemReader.FSMergeReader;
import kieker.tpan.reader.filesystemReader.FSReader;
import kieker.common.record.AbstractMonitoringRecord;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Andre van Hoorn
 */
public class FSReaderRealtime extends AbstractKiekerMonitoringLogReader {

    private static final Log log = LogFactory.getLog(FSReaderRealtime.class);

    /* delegate */
    private AbstractKiekerMonitoringLogReader fsReader;
    private RealtimeReplayDistributor rtDistributor = null;

    private static final String PROP_NAME_NUM_WORKERS = "numWorkers";
    private static final String PROP_NAME_INPUTDIRNAMES = "inputDirNames";

    /**
     * Acts as a consumer to the rtDistributor and delegates incoming records
     * to the FSReaderRealtime instance.
     */
    private class FSReaderRealtimeCons implements IKiekerRecordConsumer {

        private final FSReaderRealtime master;

        public FSReaderRealtimeCons(FSReaderRealtime master) {
            this.master = master;
        }

        public String[] getRecordTypeSubscriptionList() {
            return null;
        }

        public void consumeMonitoringRecord(AbstractMonitoringRecord monitoringRecord) throws RecordConsumerExecutionException {
            try {
                this.master.deliverRecordToConsumers(monitoringRecord);
            } catch (LogReaderExecutionException ex) {
                log.info("LogReaderExecutionException", ex);
                throw new RecordConsumerExecutionException("LogReaderExecutionException", ex);
            }
        }

        public boolean execute() throws RecordConsumerExecutionException {
            /* do nothing */
            return true;
        }

        public void terminate() {
            this.master.terminate();
        }
    }

    /** Constructor for FSReaderRealtime. Requires a subsequent call to the init
     *  method in order to specify the input directory and number of workers
     *  using the parameter @a inputDirName. */
    public FSReaderRealtime() {
    }

    /** Valid key/value pair: inputDirNames=INPUTDIRECTORY1;...;INPUTDIRECTORYN | numWorkers=XX */
    public void init(String initString) throws IllegalArgumentException {
        super.initVarsFromInitString(initString);

        String numWorkersString = this.getInitProperty(PROP_NAME_NUM_WORKERS);
        int numWorkers = -1;
        if (numWorkersString == null) {
            throw new IllegalArgumentException("Missing init parameter '"+PROP_NAME_NUM_WORKERS+"'");
        }
        try {
            numWorkers = Integer.parseInt(numWorkersString);
        } catch (NumberFormatException ex) { /* value of numWorkers remains -1 */ }

        initInstanceFromArgs(inputDirNameListToArray(this.getInitProperty(PROP_NAME_INPUTDIRNAMES)), numWorkers);
    }

    public FSReaderRealtime(final String[] inputDirNames, int numWorkers) {
        initInstanceFromArgs(inputDirNames, numWorkers);
    }

    private String[] inputDirNameListToArray(final String inputDirNameList) throws IllegalArgumentException{
       String[] dirNameArray;

        // parse inputDir property value
       if (inputDirNameList == null || inputDirNameList.trim().length() == 0){
           log.error("Invalid argument value for inputDirNameList:" + inputDirNameList);
           throw new IllegalArgumentException("Invalid argument value for inputDirNameList:" + inputDirNameList);
       }
        try {
            StringTokenizer dirNameTokenizer = new StringTokenizer(inputDirNameList, ";");
            dirNameArray = new String[dirNameTokenizer.countTokens()];
            for (int i = 0; dirNameTokenizer.hasMoreTokens(); i++) {
                dirNameArray[i] = dirNameTokenizer.nextToken().trim();
            }
        } catch (Exception exc) {
            throw new IllegalArgumentException("Error parsing list of input directories'" + inputDirNameList + "'", exc);
        }
       return dirNameArray;
    }

    private void initInstanceFromArgs(final String[] inputDirNames, int numWorkers) throws IllegalArgumentException {
        if (inputDirNames == null || inputDirNames.length <= 0) {
            throw new IllegalArgumentException("Invalid property value for "+PROP_NAME_INPUTDIRNAMES+":" + inputDirNames);
        }

        if (numWorkers <= 0) {
            throw new IllegalArgumentException("Invalid proprty value for "+PROP_NAME_NUM_WORKERS+": " + numWorkers);
        }

        if (inputDirNames.length == 1){
            // faster since there's no threading overhead
            fsReader = new FSReader(inputDirNames[0]);
        } else {
            fsReader = new FSMergeReader(inputDirNames);
        }
        IKiekerRecordConsumer rtCons = new FSReaderRealtimeCons(this);
        rtDistributor = new RealtimeReplayDistributor(numWorkers, rtCons);
        fsReader.addConsumer(rtDistributor, null);
    }

    public boolean execute() throws LogReaderExecutionException {
        return this.fsReader.execute();
    }
}
