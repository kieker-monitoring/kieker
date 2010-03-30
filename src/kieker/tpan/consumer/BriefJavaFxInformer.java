/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *
 */

package kieker.tpan.consumer;

import kieker.common.record.IMonitoringRecord;
import kieker.tpan.datamodel.MessageTrace;
import kieker.tpan.plugins.traceReconstruction.IExecutionTraceReceiver;
import kieker.tpan.plugins.traceReconstruction.IMessageTraceReceiver;
import kieker.tpan.plugins.traceReconstruction.TraceProcessingException;
import kieker.common.record.OperationExecutionRecord;

/**
 *
 * @author matthias
 */
public class BriefJavaFxInformer implements IMonitoringRecordConsumer, IMessageTraceReceiver {

    public BriefJavaFxInformer() {
        try {
            System.out.println("==> Trying to start JavaFx window");
            String javaFxWindowClassname = "KiekerLiveAnalyzer.JavaMain";
            Object obj = Class.forName(javaFxWindowClassname).newInstance();
            jfxRc = (IMonitoringRecordConsumer) obj;
            jfxTr = (IMessageTraceReceiver) obj; // dont wonder, its the same object twice
            System.out.println("==> Success to start JavaFx window (at least the class invocation)");
        } catch (Exception e) {
            System.out.println("==> Failed to execute JavaFx window = failed to execute Class.forName(KiekerLiveAnalyzer.JavaMain)  - most likely you do not have KiekerLiveAnalyzer.jar in the classpath (put it or link it in your tpan-plugins folder.) \n Message:"+e.getMessage());
            e.printStackTrace();
            System.out.println("==> Failed to start JavaFx window");
            //throw new MonitoringRecordConsumerExecutionException(errorMessage,e);
        }
    }

    /** Consuming only execution records */
    private final static String[] recordTypeSubscriptionList = {
        OperationExecutionRecord.class.getName()
    };

    public String[] getRecordTypeSubscriptionList() {
        return null; // null gets it all
    }

    public void consumeMonitoringRecord(IMonitoringRecord monitoringRecord) throws MonitoringRecordConsumerExecutionException {
       // System.out.println("BriefJavaFxInformer.consumeMonitoringRecord(...)");
        if (jfxRc == null) {
            System.out.println("WARNING: BriefJavaFxInformer.consumeMonitoringRecord called without execute() first - ignoring message");
        } else {
            jfxRc.consumeMonitoringRecord(monitoringRecord);
        }
    }
    // these variables represents access to the javafx window
    IMonitoringRecordConsumer jfxRc = null;
    IMessageTraceReceiver jfxTr = null;
    IMessageTraceReceiver jfxUniqueTr = null;
    IExecutionTraceReceiver jfxBrokenExecutionTraceReceiver = null;


    public void setUniqueTraceReceiver(IMessageTraceReceiver mtr){
        jfxUniqueTr = mtr;
    }

    public IMessageTraceReceiver getJfxUniqueTr() {
        return jfxUniqueTr;
    }

    public IExecutionTraceReceiver getJfxBrokenExecutionTraceReceiver() {
        return jfxBrokenExecutionTraceReceiver;
    }

    public void setJfxBrokenExecutionTraceReceiver(IExecutionTraceReceiver jfxBrokenExecutionTraceReceiver) {
        this.jfxBrokenExecutionTraceReceiver = jfxBrokenExecutionTraceReceiver;
    }

    

    public boolean execute() throws MonitoringRecordConsumerExecutionException {
        jfxRc.execute();
        return true;
    }

    public void terminate() {
        try{
        jfxRc.terminate();
        } catch(Exception e){}
        // nothing to do
    }

    public void newTrace(MessageTrace t) throws TraceProcessingException {
        //System.out.println("BJFX new Traces"+t.getTraceId());
        jfxTr.newTrace(t);
    }
}
