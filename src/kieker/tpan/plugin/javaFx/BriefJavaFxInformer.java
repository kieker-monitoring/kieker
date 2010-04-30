package kieker.tpan.plugin.javaFx;

/*
 * ==================LICENCE=========================
 * Copyright 2006-2009 Kieker Project
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
 *
 */

import java.util.Collection;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.MonitoringRecordReceiverException;
import kieker.tpan.datamodel.MessageTrace;
import kieker.tpan.plugin.util.event.EventProcessingException;
import kieker.tpan.plugin.traceAnalysis.IInvalidExecutionTraceReceiver;
import kieker.tpan.plugin.traceAnalysis.IMessageTraceReceiver;
import kieker.common.record.OperationExecutionRecord;
import kieker.tpan.plugin.IMonitoringRecordConsumerPlugin;

/**
 *
 * @author matthias
 */
public class BriefJavaFxInformer implements IMonitoringRecordConsumerPlugin, IMessageTraceReceiver {

    public BriefJavaFxInformer() {
        try {
            System.out.println("==> Trying to start JavaFx window");
            String javaFxWindowClassname = "KiekerLiveAnalyzer.JavaMain";
            Object obj = Class.forName(javaFxWindowClassname).newInstance();
            jfxRc = (IMonitoringRecordConsumerPlugin) obj;
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

    public Collection<Class<? extends IMonitoringRecord>> getRecordTypeSubscriptionList() {
        return null; // receive records of any type
    }

    public boolean newMonitoringRecord(IMonitoringRecord monitoringRecord) throws MonitoringRecordReceiverException {
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
    IMessageTraceReceiver jfxTr = null;
    IMessageTraceReceiver jfxUniqueTr = null;
    IInvalidExecutionTraceReceiver jfxBrokenExecutionTraceReceiver = null;


    public void setUniqueTraceReceiver(IMessageTraceReceiver mtr){
        jfxUniqueTr = mtr;
    }

    public IMessageTraceReceiver getJfxUniqueTr() {
        return jfxUniqueTr;
    }

    public IInvalidExecutionTraceReceiver getJfxBrokenExecutionTraceReceiver() {
        return jfxBrokenExecutionTraceReceiver;
    }

    public void setJfxBrokenExecutionTraceReceiver(IInvalidExecutionTraceReceiver jfxBrokenExecutionTraceReceiver) {
        this.jfxBrokenExecutionTraceReceiver = jfxBrokenExecutionTraceReceiver;
    }

    

    public boolean execute() {
        //jfxRc.execute();
        return true;
    }

    public void terminate(final boolean error) {
        try{
        //jfxRc.terminate(error);
        } catch(Exception e){}
        // nothing to do
    }

    public void newEvent(MessageTrace mt) throws EventProcessingException {
        //System.out.println("BJFX new Traces"+t.getTraceId());
        jfxTr.newEvent(mt);
    }
}
