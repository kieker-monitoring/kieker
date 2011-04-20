package kieker.evaluation.monitoredApplication;

import kieker.monitoring.annotation.BenchmarkProbe;
import kieker.monitoring.annotation.OperationExecutionMonitoringProbe;

/**
 * @author Jan Waller
 */
public final class MonitoredClass {

    @OperationExecutionMonitoringProbe()
    @BenchmarkProbe()
    public final long monitoredMethod(final long methodTime, final int recDepth) {
        if (recDepth > 1) {
            return monitoredMethod(methodTime, recDepth - 1);
        } else {
          final long exitTime = System.nanoTime() + methodTime;
          long currentTime = System.nanoTime();
          while (currentTime < exitTime) {
              currentTime = System.nanoTime();
          }
          return currentTime;
        }
    }
}
