package kieker.evaluation.monitoredApplication;

import kieker.tpmon.annotation.TpmonExecutionMonitoringProbe;
import kieker.tpmon.annotation.BenchmarkProbe;

/**
 * @author Jan Waller
 */
public class MonitoredClass {

    @TpmonExecutionMonitoringProbe()
    @BenchmarkProbe()
    public long monitoredMethod(final long methodTime) {
        final long exitTime = System.nanoTime() + methodTime;
        long currentTime = System.nanoTime();
        while (currentTime < exitTime) {
            currentTime = System.nanoTime();
        }
        return currentTime;
    }

    @TpmonExecutionMonitoringProbe()
    @BenchmarkProbe()
    public long monitoredRecursiveMethod(long methodTime, final int recDepth) {
        if (recDepth > 1) {
            return monitoredRecursiveMethod(methodTime, recDepth - 1);
        } else {
            return monitoredMethod(methodTime);
        }
    }
}
