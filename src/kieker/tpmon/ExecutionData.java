/*
 * This class is used to return multiple results to the
 * calling function of a proceed call.
 */

package kieker.tpmon;

/**
 *
 * @author avanhoorn
 */
public class ExecutionData {
    public String opname = null;
    public String componentName = null;
    public long tin = -1;
    public long tout = -1;
    public boolean isEntryPoint = false;
    public long traceId = -1;
    public Object retVal = null;
}
