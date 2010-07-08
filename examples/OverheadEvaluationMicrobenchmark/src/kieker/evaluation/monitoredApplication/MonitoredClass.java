package kieker.evaluation.monitoredApplication;

import kieker.monitoring.annotation.BenchmarkProbe;
import kieker.monitoring.annotation.OperationExecutionMonitoringProbe;

/**
 * @author Jan Waller
 */
public class MonitoredClass {

    @OperationExecutionMonitoringProbe()
    @BenchmarkProbe()
    public long monitoredMethod(final long methodTime) {
        final long exitTime = System.nanoTime() + methodTime;
        long currentTime = System.nanoTime();
        while (currentTime < exitTime) {
            currentTime = System.nanoTime();
        }
        return currentTime;
    }

    @OperationExecutionMonitoringProbe()
    @BenchmarkProbe()
    public long monitoredRecursiveMethod(long methodTime, final int recDepth) {
        if (recDepth > 1) {
            return monitoredRecursiveMethod(methodTime, recDepth - 1);
        } else {
            return monitoredMethod(methodTime);
        }
    }
}
