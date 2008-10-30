/*
 * This class is used to return multiple results to the
 * calling function of a proceed call.
 */
package kieker.tpmon;

import java.io.Serializable;
import kieker.tpmon.annotations.TpmonInternal;

/**
 *
 * @author avanhoorn
 */
public class KiekerExecutionRecord implements Serializable {

    public int experimentId = -1;
    public String vmName = null;
    public String componentName = null;
    public String opname = null;
    public String sessionId = null;
    public long traceId = -1;
    public long tin = -1;
    public long tout = -1;
    public int eoi = -1;
    public int ess = -1;
    public boolean isEntryPoint = false;
    public Object retVal = null;

    static final long serialVersionUID = 117L;
    
    /**
     * Constructor private such that instances are only created by calling 
     * the static method getInstance().
     * The reason is that we might eventually introduce an object pool in 
     * order to avoid the frequent creation of KiekerExecutionRecord objects.
     */
    private KiekerExecutionRecord() {
    }

    /**
     * Returns in instance of KiekerExecutionRecord. 
     * The member variables are initialized that way that only actually
     * used variables must be updated.
     * Do not set unused member variables to dummy values such as -1 etc.!
     * 
     * @return
     */
    @TpmonInternal()
    public static KiekerExecutionRecord getInstance() {
        return new KiekerExecutionRecord();
    }

    @TpmonInternal()
    public static KiekerExecutionRecord getInstance(
            String componentName, String methodName,
            long traceId) {
        KiekerExecutionRecord execData = getInstance();
        execData.componentName = componentName;
        execData.opname = methodName;
        execData.traceId = traceId;
        return execData;
    }

    @TpmonInternal()
    public static KiekerExecutionRecord getInstance(
            String componentName, String opName,
            String sessionId, long traceId,
            long tin, long tout) {
        KiekerExecutionRecord execData = getInstance();
        execData.componentName = componentName;
        execData.opname = opName;
        execData.sessionId = sessionId;
        execData.traceId = traceId;
        execData.tin = tin;
        execData.tout = tout;
        return execData;
    }
    
    @TpmonInternal()
   public static KiekerExecutionRecord getInstance(
            String componentName, String opName,
            long traceId,
            long tin, long tout) {
        KiekerExecutionRecord execData = getInstance();
        execData.componentName = componentName;
        execData.opname = opName;
        execData.traceId = traceId;
        execData.tin = tin;
        execData.tout = tout;
        return execData;
    }
    
    @TpmonInternal()
    public static KiekerExecutionRecord getInstance(
            String componentName, String opName,
            String sessionId, long traceId,
            long tin, long tout,
            int eoi, int ess) {
        KiekerExecutionRecord execData = getInstance();
        execData.componentName = componentName;
        execData.opname = opName;
        execData.sessionId = sessionId;
        execData.traceId = traceId;
        execData.tin = tin;
        execData.tout = tout;
        execData.eoi = eoi;
        execData.ess = ess;
        return execData;
    }

    /**
     * Returns the Kieker CSV record for the object.
     */
    @TpmonInternal()
    public String toKiekerCSVRecord() {
        StringBuilder strB = new StringBuilder();
        strB.append(this.experimentId); strB.append(';');
        // concatenate opname
        strB.append(this.componentName); 
        strB.append('.');
        strB.append(this.opname);
        strB.append(';');
        // end concat opname
        strB.append(this.sessionId); strB.append(';');
        strB.append(this.traceId); strB.append(';');
        strB.append(this.tin); strB.append(';');
        strB.append(this.tout); strB.append(';');
        strB.append(this.vmName); strB.append(';');
        strB.append(this.eoi); strB.append(';');
        strB.append(this.ess); 
        return strB.toString();
    }
}
