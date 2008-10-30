package kieker.tpmon;
import kieker.tpmon.annotations.TpmonInternal;

/**
 * @author voorn
 */
public abstract class AbstractMonitoringDataWriter implements IMonitoringDataWriter {

    private boolean debugEnabled;

    @TpmonInternal()
    public abstract boolean insertMonitoringDataNow(ExecutionData execData);

    @TpmonInternal()
    public void setDebug(boolean debug) {
        this.debugEnabled = debug;
    }

    @TpmonInternal()
    public boolean isDebug() {
        return this.debugEnabled;
    }
}
