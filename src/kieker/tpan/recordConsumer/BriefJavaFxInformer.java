/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *
 */

package kieker.tpan.recordConsumer;

import kieker.common.logReader.IKiekerRecordConsumer;
import kieker.common.logReader.RecordConsumerExecutionException;
import kieker.tpmon.monitoringRecord.AbstractKiekerMonitoringRecord;
import kieker.tpmon.monitoringRecord.executions.KiekerExecutionRecord;

/**
 *
 * @author matthias
 */
public class BriefJavaFxInformer implements IKiekerRecordConsumer {

    /** Consuming only execution records */
    private final static String[] recordTypeSubscriptionList = {
        KiekerExecutionRecord.class.getName()
    };

    public String[] getRecordTypeSubscriptionList() {
        return recordTypeSubscriptionList;
    }

    public void consumeMonitoringRecord(AbstractKiekerMonitoringRecord monitoringRecord) throws RecordConsumerExecutionException {
        System.out.println("BriefJavaFxInformer.consumeMonitoringRecord(...)");
    }

    public boolean execute() throws RecordConsumerExecutionException {
        try {
            System.out.println("==> Trying to start JavaFx window");
            String javaFxWindowClassname = "KiekerLiveAnalyzer.JavaMain";
            IKiekerRecordConsumer jfxRc = (IKiekerRecordConsumer)Class.forName(javaFxWindowClassname).newInstance();
            jfxRc.execute();
            System.out.println("==> Success to start JavaFx window (at least the class invocation)");
        } catch (Exception e) {
            String errorMessage = "==> Failed to execute JavaFx window = failed to execute Class.forName(KiekerLiveAnalyzer.JavaMain)  - most likely you do not have KiekerLiveAnalyzer.jar in the classpath (put it or link it in your tpan-plugins folder.) \n Message:"+e.getMessage();
            e.printStackTrace();
            System.out.println("==> Failed to start JavaFx window");
            throw new RecordConsumerExecutionException(errorMessage);

        }



        return true;
    }

    public void terminate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
