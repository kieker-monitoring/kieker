package kieker.tools.javaFx;

import java.util.Collection;
import kieker.tools.traceAnalysis.systemModel.InvalidExecutionTrace;
import kieker.common.record.IMonitoringRecord;
import kieker.tools.traceAnalysis.systemModel.MessageTrace;
import kieker.common.record.OperationExecutionRecord;
import kieker.analysis.plugin.IMonitoringRecordConsumerPlugin;
import kieker.analysis.plugin.configuration.AbstractInputPort;
import kieker.analysis.plugin.configuration.IInputPort;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author matthias
 */
public class BriefJavaFxInformer implements IMonitoringRecordConsumerPlugin {

    private static final Log log = LogFactory.getLog(BriefJavaFxInformer.class);

    public BriefJavaFxInformer() {
        try {
            System.out.println("==> Trying to start JavaFx window");
            String javaFxWindowClassname = "KiekerLiveAnalyzer.JavaMain";
            Object obj = Class.forName(javaFxWindowClassname).newInstance();
            jfxRc = (IMonitoringRecordConsumerPlugin) obj;
//            jfxTr = (IMessageTraceReceiver) obj; // dont wonder, its the same object twice
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

    public Collection<Class<? extends IMonitoringRecord>> getRecordTypeSubscriptionList() {
        return null; // receive records of any type
    }

    public boolean newMonitoringRecord(IMonitoringRecord monitoringRecord) {
       // System.out.println("BriefJavaFxInformer.consumeMonitoringRecord(...)");
        if (jfxRc == null) {
            System.out.println("WARNING: BriefJavaFxInformer.consumeMonitoringRecord called without execute() first - ignoring message");
            return false;
        } else {
            jfxRc.newMonitoringRecord(monitoringRecord);
        }
        return true;
    }
    // these variables represents access to the javafx window
    IMonitoringRecordConsumerPlugin jfxRc = null;
//    IMessageTraceReceiver jfxTr = null;
//    IMessageTraceReceiver jfxUniqueTr = null;
//    IInvalidExecutionTraceReceiver jfxBrokenExecutionTraceReceiver = null;


//    public void setUniqueTraceReceiver(IMessageTraceReceiver mtr){
//        jfxUniqueTr = mtr;
//    }
//
//    public IMessageTraceReceiver getJfxUniqueTr() {
//        return jfxUniqueTr;
//    }
//
//    public IInvalidExecutionTraceReceiver getJfxBrokenExecutionTraceReceiver() {
//        return jfxBrokenExecutionTraceReceiver;
//    }
//
//    public void setJfxBrokenExecutionTraceReceiver(IInvalidExecutionTraceReceiver jfxBrokenExecutionTraceReceiver) {
//        this.jfxBrokenExecutionTraceReceiver = jfxBrokenExecutionTraceReceiver;
//    }
    
    @Override
    public boolean execute() {
        //jfxRc.execute();
        return true;
    }

    @Override
    public void terminate(final boolean error) {
        try{
        //jfxRc.terminate(error);
        } catch(Exception e){}
        // nothing to do
    }

    private final IInputPort<MessageTrace> jfxUniqueMessageTraceInputPort =
            new AbstractInputPort<MessageTrace>("Broken execution traces") {

        @Override
        public void newEvent(MessageTrace et) {
            // TODO: handle new events here
//            try {
//                //jfxUniqueTr.newEvent(et);
//            } catch (EventProcessingException ex) {
//                log.error("EventProcessingException", ex);
//            }
        }
    };

    public IInputPort<MessageTrace> getJfxUniqueMessageTraceInputPort(){
        return this.jfxUniqueMessageTraceInputPort;
    }

    private final IInputPort<InvalidExecutionTrace> jfxBrokenExecutionTraceInputPort =
            new AbstractInputPort<InvalidExecutionTrace>("Broken execution traces") {

        @Override
        public void newEvent(InvalidExecutionTrace et) {
              // TODO: handle new events here
//            try {
//                //jfxBrokenExecutionTraceReceiver.newEvent(et);
//            } catch (EventProcessingException ex) {
//                log.error("EventProcessingException", ex);
//            }
        }
    };

    public IInputPort<InvalidExecutionTrace> getJfxBrokenExecutionTraceInputPort(){
        return this.jfxBrokenExecutionTraceInputPort;
    }

    private final IInputPort<MessageTrace> messageTraceInputPort =
            new AbstractInputPort<MessageTrace>("Message traces") {

        @Override
        public void newEvent(MessageTrace mt) {
            // TODO: handle new events here
//            try {
//                //System.out.println("BJFX new Traces"+t.getTraceId());
//                //jfxTr.newEvent(mt);
//            } catch (EventProcessingException ex) {
//                log.error("EventProcessingException", ex);
//            }
        }
    };

    public IInputPort<MessageTrace> getMessageTraceInputPort(){
        return this.messageTraceInputPort;
    }
}
